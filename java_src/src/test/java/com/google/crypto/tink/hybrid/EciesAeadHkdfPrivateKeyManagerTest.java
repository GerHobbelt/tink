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

package com.google.crypto.tink.hybrid;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.crypto.tink.HybridDecrypt;
import com.google.crypto.tink.HybridEncrypt;
import com.google.crypto.tink.KeyTemplate;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.Parameters;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AesCtrHmacAeadKeyManager;
import com.google.crypto.tink.aead.AesCtrHmacAeadParameters;
import com.google.crypto.tink.aead.AesGcmParameters;
import com.google.crypto.tink.internal.KeyTypeManager;
import com.google.crypto.tink.proto.EcPointFormat;
import com.google.crypto.tink.proto.EciesAeadDemParams;
import com.google.crypto.tink.proto.EciesAeadHkdfKeyFormat;
import com.google.crypto.tink.proto.EciesAeadHkdfParams;
import com.google.crypto.tink.proto.EciesAeadHkdfPrivateKey;
import com.google.crypto.tink.proto.EciesAeadHkdfPublicKey;
import com.google.crypto.tink.proto.EciesHkdfKemParams;
import com.google.crypto.tink.proto.EllipticCurveType;
import com.google.crypto.tink.proto.HashType;
import com.google.crypto.tink.proto.KeyData.KeyMaterialType;
import com.google.crypto.tink.proto.OutputPrefixType;
import com.google.crypto.tink.subtle.EciesAeadHkdfDemHelper;
import com.google.crypto.tink.subtle.EciesAeadHkdfHybridEncrypt;
import com.google.crypto.tink.subtle.EllipticCurves;
import com.google.crypto.tink.subtle.Hex;
import com.google.crypto.tink.subtle.Random;
import com.google.protobuf.ByteString;
import java.security.GeneralSecurityException;
import java.security.interfaces.ECPublicKey;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/** Tests for EciesAeadHkdfPrivateKeyManager. */
@RunWith(Theories.class)
public class EciesAeadHkdfPrivateKeyManagerTest {
  @BeforeClass
  public static void setUp() throws Exception {
    AeadConfig.register();
    HybridConfig.register();
  }

  private final EciesAeadHkdfPrivateKeyManager manager = new EciesAeadHkdfPrivateKeyManager();
  private final KeyTypeManager.KeyFactory<EciesAeadHkdfKeyFormat, EciesAeadHkdfPrivateKey> factory =
      manager.keyFactory();

  @Test
  public void basics() throws Exception {
    assertThat(manager.getKeyType())
        .isEqualTo("type.googleapis.com/google.crypto.tink.EciesAeadHkdfPrivateKey");
    assertThat(manager.getVersion()).isEqualTo(0);
    assertThat(manager.keyMaterialType()).isEqualTo(KeyMaterialType.ASYMMETRIC_PRIVATE);
  }

  @Test
  public void validateKeyFormat_empty() throws Exception {
    assertThrows(
        GeneralSecurityException.class,
        () -> factory.validateKeyFormat(EciesAeadHkdfKeyFormat.getDefaultInstance()));
  }

  private static EciesAeadHkdfKeyFormat createKeyFormat(
      EllipticCurveType curve,
      HashType hashType,
      EcPointFormat ecPointFormat,
      KeyTemplate demKeyTemplate,
      byte[] salt) {

    return EciesAeadHkdfKeyFormat.newBuilder()
        .setParams(
            EciesAeadHkdfPrivateKeyManager.createParams(
                curve, hashType, ecPointFormat, demKeyTemplate, salt))
        .build();
  }

  @Test
  public void validateKeyFormat_valid() throws Exception {
    EciesAeadHkdfKeyFormat format =
        createKeyFormat(
            EllipticCurveType.NIST_P256,
            HashType.SHA256,
            EcPointFormat.UNCOMPRESSED,
            AesCtrHmacAeadKeyManager.aes128CtrHmacSha256Template(),
            Hex.decode("aabbccddeeff"));
    factory.validateKeyFormat(format);
  }

  @Test
  public void validateKeyFormat_noPointFormat_throws() throws Exception {
    EciesAeadHkdfKeyFormat format =
        createKeyFormat(
            EllipticCurveType.NIST_P256,
            HashType.SHA256,
            EcPointFormat.UNKNOWN_FORMAT,
            AesCtrHmacAeadKeyManager.aes128CtrHmacSha256Template(),
            Hex.decode("aabbccddeeff"));
    assertThrows(GeneralSecurityException.class, () -> factory.validateKeyFormat(format));
  }

  @Test
  public void validateKeyFormat_noDem_throws() throws Exception {
    EciesAeadHkdfKeyFormat format =
        EciesAeadHkdfKeyFormat.newBuilder()
            .setParams(
                EciesAeadHkdfParams.newBuilder()
                    .setKemParams(
                        EciesHkdfKemParams.newBuilder()
                            .setCurveType(EllipticCurveType.NIST_P256)
                            .setHkdfHashType(HashType.SHA256)
                            .setHkdfSalt(ByteString.copyFrom(Hex.decode("aabbccddeeff"))))
                    .setDemParams(
                        EciesAeadDemParams.newBuilder()
                            .setAeadDem(
                                com.google.crypto.tink.proto.KeyTemplate.newBuilder()
                                    .setOutputPrefixType(OutputPrefixType.TINK)))
                    .setEcPointFormat(EcPointFormat.UNCOMPRESSED))
            .build();
    assertThrows(GeneralSecurityException.class, () -> factory.validateKeyFormat(format));
  }

  @Test
  public void validateKeyFormat_noKemCurve_throws() throws Exception {
    EciesAeadHkdfKeyFormat format =
        createKeyFormat(
            EllipticCurveType.UNKNOWN_CURVE,
            HashType.SHA256,
            EcPointFormat.UNCOMPRESSED,
            AesCtrHmacAeadKeyManager.aes128CtrHmacSha256Template(),
            Hex.decode("aabbccddeeff"));
    assertThrows(GeneralSecurityException.class, () -> factory.validateKeyFormat(format));
  }

  @Test
  public void validateKeyFormat_noKemHash_throws() throws Exception {
    EciesAeadHkdfKeyFormat format =
        createKeyFormat(
            EllipticCurveType.NIST_P256,
            HashType.UNKNOWN_HASH,
            EcPointFormat.UNCOMPRESSED,
            AesCtrHmacAeadKeyManager.aes128CtrHmacSha256Template(),
            Hex.decode("aabbccddeeff"));
    assertThrows(GeneralSecurityException.class, () -> factory.validateKeyFormat(format));
  }

  @Test
  public void createKey_checkValues() throws Exception {
    EciesAeadHkdfKeyFormat format =
        createKeyFormat(
            EllipticCurveType.NIST_P256,
            HashType.SHA256,
            EcPointFormat.UNCOMPRESSED,
            AesCtrHmacAeadKeyManager.aes128CtrHmacSha256Template(),
            Hex.decode("aabbccddeeff"));
    EciesAeadHkdfPrivateKey key = factory.createKey(format);
    assertThat(key.getPublicKey().getParams()).isEqualTo(format.getParams());
    assertThat(key.getPublicKey().getX()).isNotEmpty();
    assertThat(key.getPublicKey().getY()).isNotEmpty();
    assertThat(key.getKeyValue()).isNotEmpty();
  }

  private EciesAeadHkdfPrivateKey createValidKey() throws Exception {
    EciesAeadHkdfKeyFormat format =
        createKeyFormat(
            EllipticCurveType.NIST_P256,
            HashType.SHA256,
            EcPointFormat.UNCOMPRESSED,
            AesCtrHmacAeadKeyManager.aes128CtrHmacSha256Template(),
            Hex.decode("aabbccddeeff"));
    return factory.createKey(format);
  }

  @Test
  public void validateKey_valid() throws Exception {
    manager.validateKey(createValidKey());
  }

  @Test
  public void validateKey_invalidVersion_throws() throws Exception {
    EciesAeadHkdfPrivateKey key =
        EciesAeadHkdfPrivateKey.newBuilder(createValidKey()).setVersion(1).build();
    assertThrows(GeneralSecurityException.class, () -> manager.validateKey(key));
  }

  @Test
  public void getPublicKey_values() throws Exception {
    EciesAeadHkdfPrivateKey key = createValidKey();
    EciesAeadHkdfPublicKey publicKey = manager.getPublicKey(key);

    assertThat(publicKey).isEqualTo(key.getPublicKey());
  }

  @Test
  public void createPrimitive() throws Exception {
    EciesAeadHkdfPrivateKey key = createValidKey();
    HybridDecrypt hybridDecrypt = manager.getPrimitive(key, HybridDecrypt.class);

    EciesAeadHkdfParams eciesParams = key.getPublicKey().getParams();
    EciesHkdfKemParams kemParams = eciesParams.getKemParams();
    ECPublicKey recipientPublicKey =
        EllipticCurves.getEcPublicKey(
            HybridUtil.toCurveType(kemParams.getCurveType()),
            key.getPublicKey().getX().toByteArray(),
            key.getPublicKey().getY().toByteArray());
    EciesAeadHkdfDemHelper demHelper =
        new RegistryEciesAeadHkdfDemHelper(eciesParams.getDemParams().getAeadDem());
    HybridEncrypt hybridEncrypt = new EciesAeadHkdfHybridEncrypt(
        recipientPublicKey,
        kemParams.getHkdfSalt().toByteArray(),
        HybridUtil.toHmacAlgo(kemParams.getHkdfHashType()),
        HybridUtil.toPointFormatType(eciesParams.getEcPointFormat()),
        demHelper);

    byte[] message = Random.randBytes(20);
    byte[] contextInfo = Random.randBytes(20);
    assertThat(hybridDecrypt.decrypt(hybridEncrypt.encrypt(message, contextInfo), contextInfo))
        .isEqualTo(message);
  }

  @Test
  public void testEciesP256HkdfHmacSha256Aes128GcmTemplate() throws Exception {
    KeyTemplate template =
        EciesAeadHkdfPrivateKeyManager.eciesP256HkdfHmacSha256Aes128GcmTemplate();
    assertThat(template.toParameters())
        .isEqualTo(
            EciesParameters.builder()
                .setCurveType(EciesParameters.CurveType.NIST_P256)
                .setHashType(EciesParameters.HashType.SHA256)
                .setNistCurvePointFormat(EciesParameters.PointFormat.UNCOMPRESSED)
                .setVariant(EciesParameters.Variant.TINK)
                .setDemParameters(
                    AesGcmParameters.builder()
                        .setIvSizeBytes(12)
                        .setKeySizeBytes(16)
                        .setTagSizeBytes(16)
                        .setVariant(AesGcmParameters.Variant.NO_PREFIX)
                        .build())
                .build());
  }

  @Test
  public void testRawEciesP256HkdfHmacSha256Aes128GcmCompressedTemplate() throws Exception {
    KeyTemplate template =
        EciesAeadHkdfPrivateKeyManager.rawEciesP256HkdfHmacSha256Aes128GcmCompressedTemplate();
    assertThat(template.toParameters())
        .isEqualTo(
            EciesParameters.builder()
                .setCurveType(EciesParameters.CurveType.NIST_P256)
                .setHashType(EciesParameters.HashType.SHA256)
                .setNistCurvePointFormat(EciesParameters.PointFormat.COMPRESSED)
                .setVariant(EciesParameters.Variant.NO_PREFIX)
                .setDemParameters(
                    AesGcmParameters.builder()
                        .setIvSizeBytes(12)
                        .setKeySizeBytes(16)
                        .setTagSizeBytes(16)
                        .setVariant(AesGcmParameters.Variant.NO_PREFIX)
                        .build())
                .build());
  }

  @Test
  public void testEciesP256HkdfHmacSha256Aes128CtrHmacSha256Template() throws Exception {
    KeyTemplate template =
        EciesAeadHkdfPrivateKeyManager.eciesP256HkdfHmacSha256Aes128CtrHmacSha256Template();

    assertThat(template.toParameters())
        .isEqualTo(
            EciesParameters.builder()
                .setCurveType(EciesParameters.CurveType.NIST_P256)
                .setHashType(EciesParameters.HashType.SHA256)
                .setNistCurvePointFormat(EciesParameters.PointFormat.UNCOMPRESSED)
                .setVariant(EciesParameters.Variant.TINK)
                .setDemParameters(
                    AesCtrHmacAeadParameters.builder()
                        .setAesKeySizeBytes(16)
                        .setHmacKeySizeBytes(32)
                        .setTagSizeBytes(16)
                        .setIvSizeBytes(16)
                        .setHashType(AesCtrHmacAeadParameters.HashType.SHA256)
                        .setVariant(AesCtrHmacAeadParameters.Variant.NO_PREFIX)
                        .build())
                .build());
  }

  @Test
  public void testRawEciesP256HkdfHmacSha256Aes128CtrHmacSha256CompressedTemplate()
      throws Exception {
    KeyTemplate template =
        EciesAeadHkdfPrivateKeyManager
            .rawEciesP256HkdfHmacSha256Aes128CtrHmacSha256CompressedTemplate();

    assertThat(template.toParameters())
        .isEqualTo(
            EciesParameters.builder()
                .setCurveType(EciesParameters.CurveType.NIST_P256)
                .setHashType(EciesParameters.HashType.SHA256)
                .setNistCurvePointFormat(EciesParameters.PointFormat.COMPRESSED)
                .setVariant(EciesParameters.Variant.NO_PREFIX)
                .setDemParameters(
                    AesCtrHmacAeadParameters.builder()
                        .setAesKeySizeBytes(16)
                        .setHmacKeySizeBytes(32)
                        .setTagSizeBytes(16)
                        .setIvSizeBytes(16)
                        .setHashType(AesCtrHmacAeadParameters.HashType.SHA256)
                        .setVariant(AesCtrHmacAeadParameters.Variant.NO_PREFIX)
                        .build())
                .build());
  }

  @Test
  public void testKeyTemplatesWork() throws Exception {
    Parameters p =
        EciesAeadHkdfPrivateKeyManager.eciesP256HkdfHmacSha256Aes128GcmTemplate().toParameters();
    assertThat(KeysetHandle.generateNew(p).getAt(0).getKey().getParameters()).isEqualTo(p);

    p =
        EciesAeadHkdfPrivateKeyManager.rawEciesP256HkdfHmacSha256Aes128GcmCompressedTemplate()
            .toParameters();
    assertThat(KeysetHandle.generateNew(p).getAt(0).getKey().getParameters()).isEqualTo(p);

    p =
        EciesAeadHkdfPrivateKeyManager.eciesP256HkdfHmacSha256Aes128CtrHmacSha256Template()
            .toParameters();
    assertThat(KeysetHandle.generateNew(p).getAt(0).getKey().getParameters()).isEqualTo(p);

    p =
        EciesAeadHkdfPrivateKeyManager
            .rawEciesP256HkdfHmacSha256Aes128CtrHmacSha256CompressedTemplate()
            .toParameters();
    assertThat(KeysetHandle.generateNew(p).getAt(0).getKey().getParameters()).isEqualTo(p);
  }

  @DataPoints("templateNames")
  public static final String[] KEY_TEMPLATES =
      new String[] {
        "ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM",
        "ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM_RAW",
        "ECIES_P256_COMPRESSED_HKDF_HMAC_SHA256_AES128_GCM",
        "ECIES_P256_COMPRESSED_HKDF_HMAC_SHA256_AES128_GCM_RAW",
        "ECIES_P256_HKDF_HMAC_SHA256_AES128_CTR_HMAC_SHA256",
        "ECIES_P256_HKDF_HMAC_SHA256_AES128_CTR_HMAC_SHA256_RAW",
        "ECIES_P256_COMPRESSED_HKDF_HMAC_SHA256_AES128_CTR_HMAC_SHA256",
        "ECIES_P256_COMPRESSED_HKDF_HMAC_SHA256_AES128_CTR_HMAC_SHA256_RAW",
      };

  @Theory
  public void testTemplates(@FromDataPoints("templateNames") String templateName) throws Exception {
    KeysetHandle h = KeysetHandle.generateNew(KeyTemplates.get(templateName));
    assertThat(h.size()).isEqualTo(1);
    assertThat(h.getAt(0).getKey().getParameters())
        .isEqualTo(KeyTemplates.get(templateName).toParameters());
  }
}
