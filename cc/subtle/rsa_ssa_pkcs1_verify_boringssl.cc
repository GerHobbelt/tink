// Copyright 2018 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
///////////////////////////////////////////////////////////////////////////////

#include "tink/subtle/rsa_ssa_pkcs1_verify_boringssl.h"

#include "absl/strings/str_cat.h"
#include "openssl/bn.h"
#include "openssl/digest.h"
#include "openssl/evp.h"
#include "openssl/rsa.h"
#include "tink/internal/ssl_unique_ptr.h"
#include "tink/internal/util.h"
#include "tink/subtle/common_enums.h"
#include "tink/subtle/subtle_util_boringssl.h"
#include "tink/util/errors.h"
#include "tink/util/statusor.h"

namespace crypto {
namespace tink {
namespace subtle {

// static
util::StatusOr<std::unique_ptr<RsaSsaPkcs1VerifyBoringSsl>>
RsaSsaPkcs1VerifyBoringSsl::New(
    const SubtleUtilBoringSSL::RsaPublicKey& pub_key,
    const SubtleUtilBoringSSL::RsaSsaPkcs1Params& params) {
  auto status = internal::CheckFipsCompatibility<RsaSsaPkcs1VerifyBoringSsl>();
  if (!status.ok()) return status;

  // Check hash.
  auto hash_status =
      SubtleUtilBoringSSL::ValidateSignatureHash(params.hash_type);
  if (!hash_status.ok()) {
    return hash_status;
  }
  auto sig_hash_result = SubtleUtilBoringSSL::EvpHash(params.hash_type);
  if (!sig_hash_result.ok()) return sig_hash_result.status();

  // The RSA modulus and exponent are checked as part of the conversion to
  // internal::SslUniquePtr<RSA>.
  util::StatusOr<internal::SslUniquePtr<RSA>> rsa =
      SubtleUtilBoringSSL::BoringSslRsaFromRsaPublicKey(pub_key);
  if (!rsa.ok()) {
    return rsa.status();
  }

  std::unique_ptr<RsaSsaPkcs1VerifyBoringSsl> verify(
      new RsaSsaPkcs1VerifyBoringSsl(std::move(rsa).ValueOrDie(),
                                     sig_hash_result.ValueOrDie()));
  return std::move(verify);
}

util::Status RsaSsaPkcs1VerifyBoringSsl::Verify(absl::string_view signature,
                                                absl::string_view data) const {
  // BoringSSL expects a non-null pointer for data,
  // regardless of whether the size is 0.
  data = internal::EnsureStringNonNull(data);

  auto digest_result = boringssl::ComputeHash(data, *sig_hash_);
  if (!digest_result.ok()) return digest_result.status();
  auto digest = std::move(digest_result.ValueOrDie());

  if (1 !=
      RSA_verify(EVP_MD_type(sig_hash_),
                 /*msg=*/digest.data(),
                 /*msg_len=*/digest.size(),
                 /*sig=*/reinterpret_cast<const uint8_t*>(signature.data()),
                 /*sig_len=*/signature.length(),
                 /*rsa=*/rsa_.get())) {
    // Signature is invalid.
    return util::Status(util::error::INVALID_ARGUMENT,
                        "Signature is not valid.");
  }

  return util::OkStatus();
}

}  // namespace subtle
}  // namespace tink
}  // namespace crypto