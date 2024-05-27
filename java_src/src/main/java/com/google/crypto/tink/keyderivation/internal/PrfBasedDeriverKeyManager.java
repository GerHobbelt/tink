// Copyright 2020 Google LLC
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

package com.google.crypto.tink.keyderivation.internal;

import com.google.crypto.tink.InsecureSecretKeyAccess;
import com.google.crypto.tink.Key;
import com.google.crypto.tink.Registry;
import com.google.crypto.tink.config.internal.TinkFipsUtil;
import com.google.crypto.tink.internal.KeyTypeManager;
import com.google.crypto.tink.internal.MutablePrimitiveRegistry;
import com.google.crypto.tink.internal.MutableSerializationRegistry;
import com.google.crypto.tink.internal.PrimitiveConstructor;
import com.google.crypto.tink.internal.ProtoKeySerialization;
import com.google.crypto.tink.keyderivation.PrfBasedKeyDerivationKey;
import com.google.crypto.tink.proto.KeyData;
import com.google.crypto.tink.proto.KeyData.KeyMaterialType;
import com.google.crypto.tink.proto.OutputPrefixType;
import com.google.crypto.tink.proto.PrfBasedDeriverKey;
import com.google.crypto.tink.proto.PrfBasedDeriverKeyFormat;
import com.google.crypto.tink.subtle.Validators;
import com.google.protobuf.ByteString;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.InvalidProtocolBufferException;
import java.security.GeneralSecurityException;

/** {@link com.google.crypto.tink.internal.KeyTypeManager} for {@link PrfBasedDeriverKey}. */
public final class PrfBasedDeriverKeyManager extends KeyTypeManager<PrfBasedDeriverKey> {
  private static final PrimitiveConstructor<PrfBasedKeyDerivationKey, KeyDeriver>
      PRIMITIVE_CONSTRUCTOR =
          PrimitiveConstructor.create(
              PrfBasedKeyDeriver::create, PrfBasedKeyDerivationKey.class, KeyDeriver.class);

  public PrfBasedDeriverKeyManager() {
    super(PrfBasedDeriverKey.class);
  }

  @Override
  public TinkFipsUtil.AlgorithmFipsCompatibility fipsStatus() {
    return TinkFipsUtil.AlgorithmFipsCompatibility.ALGORITHM_NOT_FIPS;
  }

  @Override
  public String getKeyType() {
    return "type.googleapis.com/google.crypto.tink.PrfBasedDeriverKey";
  }

  @Override
  public int getVersion() {
    return 0;
  }

  @Override
  public KeyMaterialType keyMaterialType() {
    return KeyMaterialType.SYMMETRIC;
  }

  @Override
  public void validateKey(PrfBasedDeriverKey key) throws GeneralSecurityException {
    Validators.validateVersion(key.getVersion(), getVersion());
    if (!key.hasPrfKey()) {
      throw new GeneralSecurityException("key.prf_key must be set");
    }
    if (!key.getParams().hasDerivedKeyTemplate()) {
      throw new GeneralSecurityException("key.params.derived_key_template must be set");
    }
  }

  @Override
  public PrfBasedDeriverKey parseKey(ByteString byteString) throws InvalidProtocolBufferException {
    return PrfBasedDeriverKey.parseFrom(byteString, ExtensionRegistryLite.getEmptyRegistry());
  }

  @Override
  public KeyFactory<PrfBasedDeriverKeyFormat, PrfBasedDeriverKey> keyFactory() {
    return new KeyFactory<PrfBasedDeriverKeyFormat, PrfBasedDeriverKey>(
        PrfBasedDeriverKeyFormat.class) {
      @Override
      public void validateKeyFormat(PrfBasedDeriverKeyFormat format)
          throws GeneralSecurityException {
        if (!format.hasPrfKeyTemplate()) {
          throw new GeneralSecurityException("format.params.prf_key_template must be set");
        }
        if (!format.getParams().hasDerivedKeyTemplate()) {
          throw new GeneralSecurityException("format.params.derived_key_template must be set");
        }
      }

      @Override
      public PrfBasedDeriverKeyFormat parseKeyFormat(ByteString byteString)
          throws InvalidProtocolBufferException {
        return PrfBasedDeriverKeyFormat.parseFrom(
            byteString, ExtensionRegistryLite.getEmptyRegistry());
      }

      @Override
      public PrfBasedDeriverKey createKey(PrfBasedDeriverKeyFormat format)
          throws GeneralSecurityException {
        KeyData prfKeyData = Registry.newKeyData(format.getPrfKeyTemplate());
        PrfBasedDeriverKey result =
            PrfBasedDeriverKey.newBuilder()
                .setVersion(getVersion())
                .setParams(format.getParams())
                .setPrfKey(prfKeyData)
                .build();
        OutputPrefixType outputPrefixType =
            result.getParams().getDerivedKeyTemplate().getOutputPrefixType();
        ProtoKeySerialization serialization =
            ProtoKeySerialization.create(
                "type.googleapis.com/google.crypto.tink.PrfBasedDeriverKey",
                result.toByteString(),
                KeyMaterialType.SYMMETRIC,
                result.getParams().getDerivedKeyTemplate().getOutputPrefixType(),
                outputPrefixType.equals(OutputPrefixType.RAW) ? null : 0);
        Key key =
            MutableSerializationRegistry.globalInstance()
                .parseKey(serialization, InsecureSecretKeyAccess.get());
        if (!(key instanceof PrfBasedKeyDerivationKey)) {
          throw new GeneralSecurityException(
              "Key parsing returned unexpected key type: " + key.getClass());
        }
        Object unused = PrfBasedKeyDeriver.create((PrfBasedKeyDerivationKey) key);
        return result;
      }
    };
  }

  public static void register(boolean newKeyAllowed) throws GeneralSecurityException {
    Registry.registerKeyManager(new PrfBasedDeriverKeyManager(), newKeyAllowed);
    MutablePrimitiveRegistry.globalInstance().registerPrimitiveConstructor(PRIMITIVE_CONSTRUCTOR);

    PrfBasedKeyDerivationKeyProtoSerialization.register();
  }
}
