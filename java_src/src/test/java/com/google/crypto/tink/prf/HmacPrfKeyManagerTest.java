// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////

package com.google.crypto.tink.prf;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.crypto.tink.InsecureSecretKeyAccess;
import com.google.crypto.tink.Key;
import com.google.crypto.tink.KeyTemplate;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.Parameters;
import com.google.crypto.tink.internal.KeyTypeManager;
import com.google.crypto.tink.internal.MutablePrimitiveRegistry;
import com.google.crypto.tink.internal.SlowInputStream;
import com.google.crypto.tink.proto.HashType;
import com.google.crypto.tink.proto.HmacPrfKey;
import com.google.crypto.tink.proto.HmacPrfKeyFormat;
import com.google.crypto.tink.proto.HmacPrfParams;
import com.google.crypto.tink.subtle.Hex;
import com.google.crypto.tink.subtle.PrfHmacJce;
import com.google.crypto.tink.subtle.Random;
import com.google.crypto.tink.util.SecretBytes;
import com.google.protobuf.ByteString;
import java.io.ByteArrayInputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import javax.crypto.spec.SecretKeySpec;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/** Unit tests for {@link HmacPrfKeyManager}. */
@RunWith(Theories.class)
public class HmacPrfKeyManagerTest {
  private final HmacPrfKeyManager manager = new HmacPrfKeyManager();
  private final KeyTypeManager.KeyFactory<HmacPrfKeyFormat, HmacPrfKey> factory =
      manager.keyFactory();

  @Before
  public void register() throws Exception {
    PrfConfig.register();
  }

  @Test
  public void validateKeyFormat_empty() throws Exception {
    assertThrows(
        GeneralSecurityException.class,
        () -> factory.validateKeyFormat(HmacPrfKeyFormat.getDefaultInstance()));
  }

  private static HmacPrfKeyFormat makeHmacPrfKeyFormat(int keySize, HashType hashType) {
    HmacPrfParams params = HmacPrfParams.newBuilder().setHash(hashType).build();
    return HmacPrfKeyFormat.newBuilder().setParams(params).setKeySize(keySize).build();
  }

  @Test
  public void validateKeyFormat_keySizes() throws Exception {
    factory.validateKeyFormat(makeHmacPrfKeyFormat(16, HashType.SHA256));
    assertThrows(
        GeneralSecurityException.class,
        () -> factory.validateKeyFormat(makeHmacPrfKeyFormat(15, HashType.SHA256)));
  }

  @Test
  public void createKey_valid() throws Exception {
    manager.validateKey(factory.createKey(makeHmacPrfKeyFormat(16, HashType.SHA1)));
    manager.validateKey(factory.createKey(makeHmacPrfKeyFormat(20, HashType.SHA1)));
    manager.validateKey(factory.createKey(makeHmacPrfKeyFormat(32, HashType.SHA1)));
    manager.validateKey(factory.createKey(makeHmacPrfKeyFormat(16, HashType.SHA256)));
    manager.validateKey(factory.createKey(makeHmacPrfKeyFormat(32, HashType.SHA256)));
    manager.validateKey(factory.createKey(makeHmacPrfKeyFormat(16, HashType.SHA512)));
    manager.validateKey(factory.createKey(makeHmacPrfKeyFormat(32, HashType.SHA512)));
    manager.validateKey(factory.createKey(makeHmacPrfKeyFormat(64, HashType.SHA512)));
  }

  @Test
  public void createKey_checkValues() throws Exception {
    HmacPrfKeyFormat keyFormat = makeHmacPrfKeyFormat(16, HashType.SHA256);
    HmacPrfKey key = factory.createKey(keyFormat);
    assertThat(key.getKeyValue()).hasSize(keyFormat.getKeySize());
    assertThat(key.getParams().getHash()).isEqualTo(keyFormat.getParams().getHash());
  }

  @Test
  public void createKey_multipleTimes() throws Exception {
    HmacPrfKeyFormat keyFormat = makeHmacPrfKeyFormat(16, HashType.SHA256);
    int numKeys = 100;
    Set<String> keys = new TreeSet<String>();
    for (int i = 0; i < numKeys; ++i) {
      keys.add(Hex.encode(factory.createKey(keyFormat).getKeyValue().toByteArray()));
    }
    assertThat(keys).hasSize(numKeys);
  }

  @Test
  public void validateKey_wrongVersion_throws() throws Exception {
    HmacPrfKey validKey = factory.createKey(makeHmacPrfKeyFormat(16, HashType.SHA1));
    assertThrows(
        GeneralSecurityException.class,
        () -> manager.validateKey(HmacPrfKey.newBuilder(validKey).setVersion(1).build()));
  }

  @Test
  public void validateKey_notValid_throws() throws Exception {
    HmacPrfKey validKey = factory.createKey(makeHmacPrfKeyFormat(16, HashType.SHA1));
    assertThrows(
        GeneralSecurityException.class,
        () ->
            manager.validateKey(
                HmacPrfKey.newBuilder(validKey)
                    .setKeyValue(ByteString.copyFrom(Random.randBytes(15)))
                    .build()));
    assertThrows(
        GeneralSecurityException.class,
        () ->
            manager.validateKey(
                HmacPrfKey.newBuilder(validKey)
                    .setParams(
                        HmacPrfParams.newBuilder(validKey.getParams())
                            .setHash(HashType.UNKNOWN_HASH)
                            .build())
                    .build()));
  }

  @Test
  public void getPrimitive_worksForSha1() throws Exception {
    HmacPrfKey validKey = factory.createKey(makeHmacPrfKeyFormat(16, HashType.SHA1));
    Prf managerPrf = manager.getPrimitive(validKey, Prf.class);
    Prf directPrf =
        new PrfHmacJce("HMACSHA1", new SecretKeySpec(validKey.getKeyValue().toByteArray(), "HMAC"));
    byte[] message = Random.randBytes(50);
    assertThat(managerPrf.compute(message, 19)).isEqualTo(directPrf.compute(message, 19));
  }

  @Test
  public void getPrimitive_worksForSha256() throws Exception {
    HmacPrfKey validKey = factory.createKey(makeHmacPrfKeyFormat(16, HashType.SHA256));
    Prf managerPrf = manager.getPrimitive(validKey, Prf.class);
    Prf directPrf =
        new PrfHmacJce(
            "HMACSHA256", new SecretKeySpec(validKey.getKeyValue().toByteArray(), "HMAC"));
    byte[] message = Random.randBytes(50);
    assertThat(managerPrf.compute(message, 29)).isEqualTo(directPrf.compute(message, 29));
  }

  @Test
  public void getPrimitive_worksForSha512() throws Exception {
    HmacPrfKey validKey = factory.createKey(makeHmacPrfKeyFormat(16, HashType.SHA512));
    Prf managerPrf = manager.getPrimitive(validKey, Prf.class);
    Prf directPrf =
        new PrfHmacJce(
            "HMACSHA512", new SecretKeySpec(validKey.getKeyValue().toByteArray(), "HMAC"));
    byte[] message = Random.randBytes(50);
    assertThat(managerPrf.compute(message, 33)).isEqualTo(directPrf.compute(message, 33));
  }

  @Test
  public void testHmacSha256Template() throws Exception {
    KeyTemplate template = HmacPrfKeyManager.hmacSha256Template();
    assertThat(template.toParameters())
        .isEqualTo(
            HmacPrfParameters.builder()
                .setKeySizeBytes(32)
                .setHashType(HmacPrfParameters.HashType.SHA256)
                .build());
  }

  @Test
  public void testHmacSha512Template() throws Exception {
    KeyTemplate template = HmacPrfKeyManager.hmacSha512Template();
    assertThat(template.toParameters())
        .isEqualTo(
            HmacPrfParameters.builder()
                .setKeySizeBytes(64)
                .setHashType(HmacPrfParameters.HashType.SHA512)
                .build());
  }

  @Test
  public void testKeyTemplateAndManagerCompatibility() throws Exception {
    Parameters p = HmacPrfKeyManager.hmacSha256Template().toParameters();
    assertThat(KeysetHandle.generateNew(p).getAt(0).getKey().getParameters()).isEqualTo(p);

    p = HmacPrfKeyManager.hmacSha256Template().toParameters();
    assertThat(KeysetHandle.generateNew(p).getAt(0).getKey().getParameters()).isEqualTo(p);
  }

  @DataPoints("templateNames")
  public static final String[] KEY_TEMPLATES = new String[] {"HMAC_SHA256_PRF", "HMAC_SHA512_PRF"};

  @Theory
  public void testTemplates(@FromDataPoints("templateNames") String templateName) throws Exception {
    KeysetHandle h = KeysetHandle.generateNew(KeyTemplates.get(templateName));
    assertThat(h.size()).isEqualTo(1);
    assertThat(h.getAt(0).getKey().getParameters())
        .isEqualTo(KeyTemplates.get(templateName).toParameters());
  }

  @Test
  public void registersPrfPrimitiveConstructor() throws Exception {
    Prf prf =
        MutablePrimitiveRegistry.globalInstance()
            .getPrimitive(
                com.google.crypto.tink.prf.HmacPrfKey.builder()
                    .setParameters(
                        HmacPrfParameters.builder()
                            .setHashType(HmacPrfParameters.HashType.SHA256)
                            .setKeySizeBytes(32)
                            .build())
                    .setKeyBytes(SecretBytes.randomBytes(32))
                    .build(),
                Prf.class);

    assertThat(prf).isInstanceOf(PrfHmacJce.class);
  }

  @Theory
  public void testCreateKeyFromRandomness(@FromDataPoints("templateNames") String templateName)
      throws Exception {
    byte[] keyMaterial =
        new byte[] {
          0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
          25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,
          47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68,
        };
    HmacPrfParameters parameters =
        (HmacPrfParameters) KeyTemplates.get(templateName).toParameters();
    com.google.crypto.tink.prf.HmacPrfKey key =
        HmacPrfKeyManager.createHmacKeyFromRandomness(
            parameters, new ByteArrayInputStream(keyMaterial), null, InsecureSecretKeyAccess.get());
    byte[] expectedKeyBytes = Arrays.copyOf(keyMaterial, parameters.getKeySizeBytes());
    Key expectedKey =
        com.google.crypto.tink.prf.HmacPrfKey.builder()
            .setParameters(parameters)
            .setKeyBytes(SecretBytes.copyFrom(expectedKeyBytes, InsecureSecretKeyAccess.get()))
            .build();
    assertTrue(key.equalsKey(expectedKey));
  }

  @Test
  public void testCreateKeyFromRandomness_slowInputStream_works() throws Exception {
    byte[] keyMaterial =
        new byte[] {
          0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
          25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,
          47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68,
        };
    HmacPrfParameters parameters =
        HmacPrfParameters.builder()
            .setKeySizeBytes(64)
            .setHashType(HmacPrfParameters.HashType.SHA512)
            .build();
    com.google.crypto.tink.prf.HmacPrfKey key =
        HmacPrfKeyManager.createHmacKeyFromRandomness(
            parameters, SlowInputStream.copyFrom(keyMaterial), null, InsecureSecretKeyAccess.get());
    byte[] expectedKeyBytes = Arrays.copyOf(keyMaterial, parameters.getKeySizeBytes());
    Key expectedKey =
        com.google.crypto.tink.prf.HmacPrfKey.builder()
            .setParameters(parameters)
            .setKeyBytes(SecretBytes.copyFrom(expectedKeyBytes, InsecureSecretKeyAccess.get()))
            .build();
    assertTrue(key.equalsKey(expectedKey));
  }
}
