// Copyright 2018 Google Inc.
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

// Code generated by protoc-gen-go. DO NOT EDIT.
// versions:
// 	protoc-gen-go v1.27.1
// 	protoc        v3.12.4
// source: third_party/tink/proto/jwt_rsa_ssa_pss.proto

package jwt_rsa_ssa_pss_go_proto

import (
	protoreflect "google.golang.org/protobuf/reflect/protoreflect"
	protoimpl "google.golang.org/protobuf/runtime/protoimpl"
	reflect "reflect"
	sync "sync"
)

const (
	// Verify that this generated code is sufficiently up-to-date.
	_ = protoimpl.EnforceVersion(20 - protoimpl.MinVersion)
	// Verify that runtime/protoimpl is sufficiently up-to-date.
	_ = protoimpl.EnforceVersion(protoimpl.MaxVersion - 20)
)

// See https://datatracker.ietf.org/doc/html/rfc7518#section-3.5
type JwtRsaSsaPssAlgorithm int32

const (
	JwtRsaSsaPssAlgorithm_PS_UNKNOWN JwtRsaSsaPssAlgorithm = 0
	JwtRsaSsaPssAlgorithm_PS256      JwtRsaSsaPssAlgorithm = 1 // RSASSA-PSS using SHA-256 and MGF1 with SHA-256
	JwtRsaSsaPssAlgorithm_PS384      JwtRsaSsaPssAlgorithm = 2 // RSASSA-PSS using SHA-384 and MGF1 with SHA-384
	JwtRsaSsaPssAlgorithm_PS512      JwtRsaSsaPssAlgorithm = 3 // RSASSA-PSS using SHA-512 and MGF1 with SHA-512
)

// Enum value maps for JwtRsaSsaPssAlgorithm.
var (
	JwtRsaSsaPssAlgorithm_name = map[int32]string{
		0: "PS_UNKNOWN",
		1: "PS256",
		2: "PS384",
		3: "PS512",
	}
	JwtRsaSsaPssAlgorithm_value = map[string]int32{
		"PS_UNKNOWN": 0,
		"PS256":      1,
		"PS384":      2,
		"PS512":      3,
	}
)

func (x JwtRsaSsaPssAlgorithm) Enum() *JwtRsaSsaPssAlgorithm {
	p := new(JwtRsaSsaPssAlgorithm)
	*p = x
	return p
}

func (x JwtRsaSsaPssAlgorithm) String() string {
	return protoimpl.X.EnumStringOf(x.Descriptor(), protoreflect.EnumNumber(x))
}

func (JwtRsaSsaPssAlgorithm) Descriptor() protoreflect.EnumDescriptor {
	return file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_enumTypes[0].Descriptor()
}

func (JwtRsaSsaPssAlgorithm) Type() protoreflect.EnumType {
	return &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_enumTypes[0]
}

func (x JwtRsaSsaPssAlgorithm) Number() protoreflect.EnumNumber {
	return protoreflect.EnumNumber(x)
}

// Deprecated: Use JwtRsaSsaPssAlgorithm.Descriptor instead.
func (JwtRsaSsaPssAlgorithm) EnumDescriptor() ([]byte, []int) {
	return file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescGZIP(), []int{0}
}

// key_type: type.googleapis.com/google.crypto.tink.JwtRsaSsaPssPublicKey
type JwtRsaSsaPssPublicKey struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Version   uint32                `protobuf:"varint,1,opt,name=version,proto3" json:"version,omitempty"`
	Algorithm JwtRsaSsaPssAlgorithm `protobuf:"varint,2,opt,name=algorithm,proto3,enum=google.crypto.tink.JwtRsaSsaPssAlgorithm" json:"algorithm,omitempty"`
	// Modulus.
	// Unsigned big integer in big-endian representation.
	N []byte `protobuf:"bytes,3,opt,name=n,proto3" json:"n,omitempty"`
	// Public exponent.
	// Unsigned big integer in big-endian representation.
	E         []byte                           `protobuf:"bytes,4,opt,name=e,proto3" json:"e,omitempty"`
	CustomKid *JwtRsaSsaPssPublicKey_CustomKid `protobuf:"bytes,5,opt,name=custom_kid,json=customKid,proto3" json:"custom_kid,omitempty"`
}

func (x *JwtRsaSsaPssPublicKey) Reset() {
	*x = JwtRsaSsaPssPublicKey{}
	if protoimpl.UnsafeEnabled {
		mi := &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[0]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *JwtRsaSsaPssPublicKey) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*JwtRsaSsaPssPublicKey) ProtoMessage() {}

func (x *JwtRsaSsaPssPublicKey) ProtoReflect() protoreflect.Message {
	mi := &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[0]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use JwtRsaSsaPssPublicKey.ProtoReflect.Descriptor instead.
func (*JwtRsaSsaPssPublicKey) Descriptor() ([]byte, []int) {
	return file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescGZIP(), []int{0}
}

func (x *JwtRsaSsaPssPublicKey) GetVersion() uint32 {
	if x != nil {
		return x.Version
	}
	return 0
}

func (x *JwtRsaSsaPssPublicKey) GetAlgorithm() JwtRsaSsaPssAlgorithm {
	if x != nil {
		return x.Algorithm
	}
	return JwtRsaSsaPssAlgorithm_PS_UNKNOWN
}

func (x *JwtRsaSsaPssPublicKey) GetN() []byte {
	if x != nil {
		return x.N
	}
	return nil
}

func (x *JwtRsaSsaPssPublicKey) GetE() []byte {
	if x != nil {
		return x.E
	}
	return nil
}

func (x *JwtRsaSsaPssPublicKey) GetCustomKid() *JwtRsaSsaPssPublicKey_CustomKid {
	if x != nil {
		return x.CustomKid
	}
	return nil
}

// key_type: type.googleapis.com/google.crypto.tink.JwtRsaSsaPssPrivateKey
type JwtRsaSsaPssPrivateKey struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Version   uint32                 `protobuf:"varint,1,opt,name=version,proto3" json:"version,omitempty"`
	PublicKey *JwtRsaSsaPssPublicKey `protobuf:"bytes,2,opt,name=public_key,json=publicKey,proto3" json:"public_key,omitempty"`
	// Private exponent.
	// Unsigned big integer in big-endian representation.
	D []byte `protobuf:"bytes,3,opt,name=d,proto3" json:"d,omitempty"`
	// The following parameters are used to optimize RSA signature computation.
	// The prime factor p of n.
	// Unsigned big integer in big-endian representation.
	P []byte `protobuf:"bytes,4,opt,name=p,proto3" json:"p,omitempty"`
	// The prime factor q of n.
	// Unsigned big integer in big-endian representation.
	Q []byte `protobuf:"bytes,5,opt,name=q,proto3" json:"q,omitempty"`
	// d mod (p - 1).
	// Unsigned big integer in big-endian representation.
	Dp []byte `protobuf:"bytes,6,opt,name=dp,proto3" json:"dp,omitempty"`
	// d mod (q - 1).
	// Unsigned big integer in big-endian representation.
	Dq []byte `protobuf:"bytes,7,opt,name=dq,proto3" json:"dq,omitempty"`
	// Chinese Remainder Theorem coefficient q^(-1) mod p.
	// Unsigned big integer in big-endian representation.
	Crt []byte `protobuf:"bytes,8,opt,name=crt,proto3" json:"crt,omitempty"`
}

func (x *JwtRsaSsaPssPrivateKey) Reset() {
	*x = JwtRsaSsaPssPrivateKey{}
	if protoimpl.UnsafeEnabled {
		mi := &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[1]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *JwtRsaSsaPssPrivateKey) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*JwtRsaSsaPssPrivateKey) ProtoMessage() {}

func (x *JwtRsaSsaPssPrivateKey) ProtoReflect() protoreflect.Message {
	mi := &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[1]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use JwtRsaSsaPssPrivateKey.ProtoReflect.Descriptor instead.
func (*JwtRsaSsaPssPrivateKey) Descriptor() ([]byte, []int) {
	return file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescGZIP(), []int{1}
}

func (x *JwtRsaSsaPssPrivateKey) GetVersion() uint32 {
	if x != nil {
		return x.Version
	}
	return 0
}

func (x *JwtRsaSsaPssPrivateKey) GetPublicKey() *JwtRsaSsaPssPublicKey {
	if x != nil {
		return x.PublicKey
	}
	return nil
}

func (x *JwtRsaSsaPssPrivateKey) GetD() []byte {
	if x != nil {
		return x.D
	}
	return nil
}

func (x *JwtRsaSsaPssPrivateKey) GetP() []byte {
	if x != nil {
		return x.P
	}
	return nil
}

func (x *JwtRsaSsaPssPrivateKey) GetQ() []byte {
	if x != nil {
		return x.Q
	}
	return nil
}

func (x *JwtRsaSsaPssPrivateKey) GetDp() []byte {
	if x != nil {
		return x.Dp
	}
	return nil
}

func (x *JwtRsaSsaPssPrivateKey) GetDq() []byte {
	if x != nil {
		return x.Dq
	}
	return nil
}

func (x *JwtRsaSsaPssPrivateKey) GetCrt() []byte {
	if x != nil {
		return x.Crt
	}
	return nil
}

type JwtRsaSsaPssKeyFormat struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Version           uint32                `protobuf:"varint,1,opt,name=version,proto3" json:"version,omitempty"`
	Algorithm         JwtRsaSsaPssAlgorithm `protobuf:"varint,2,opt,name=algorithm,proto3,enum=google.crypto.tink.JwtRsaSsaPssAlgorithm" json:"algorithm,omitempty"`
	ModulusSizeInBits uint32                `protobuf:"varint,3,opt,name=modulus_size_in_bits,json=modulusSizeInBits,proto3" json:"modulus_size_in_bits,omitempty"`
	PublicExponent    []byte                `protobuf:"bytes,4,opt,name=public_exponent,json=publicExponent,proto3" json:"public_exponent,omitempty"`
}

func (x *JwtRsaSsaPssKeyFormat) Reset() {
	*x = JwtRsaSsaPssKeyFormat{}
	if protoimpl.UnsafeEnabled {
		mi := &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[2]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *JwtRsaSsaPssKeyFormat) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*JwtRsaSsaPssKeyFormat) ProtoMessage() {}

func (x *JwtRsaSsaPssKeyFormat) ProtoReflect() protoreflect.Message {
	mi := &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[2]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use JwtRsaSsaPssKeyFormat.ProtoReflect.Descriptor instead.
func (*JwtRsaSsaPssKeyFormat) Descriptor() ([]byte, []int) {
	return file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescGZIP(), []int{2}
}

func (x *JwtRsaSsaPssKeyFormat) GetVersion() uint32 {
	if x != nil {
		return x.Version
	}
	return 0
}

func (x *JwtRsaSsaPssKeyFormat) GetAlgorithm() JwtRsaSsaPssAlgorithm {
	if x != nil {
		return x.Algorithm
	}
	return JwtRsaSsaPssAlgorithm_PS_UNKNOWN
}

func (x *JwtRsaSsaPssKeyFormat) GetModulusSizeInBits() uint32 {
	if x != nil {
		return x.ModulusSizeInBits
	}
	return 0
}

func (x *JwtRsaSsaPssKeyFormat) GetPublicExponent() []byte {
	if x != nil {
		return x.PublicExponent
	}
	return nil
}

// Optional, custom kid header value to be used with "RAW" keys.
// "TINK" keys with this value set will be rejected.
type JwtRsaSsaPssPublicKey_CustomKid struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Value string `protobuf:"bytes,1,opt,name=value,proto3" json:"value,omitempty"`
}

func (x *JwtRsaSsaPssPublicKey_CustomKid) Reset() {
	*x = JwtRsaSsaPssPublicKey_CustomKid{}
	if protoimpl.UnsafeEnabled {
		mi := &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[3]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *JwtRsaSsaPssPublicKey_CustomKid) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*JwtRsaSsaPssPublicKey_CustomKid) ProtoMessage() {}

func (x *JwtRsaSsaPssPublicKey_CustomKid) ProtoReflect() protoreflect.Message {
	mi := &file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[3]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use JwtRsaSsaPssPublicKey_CustomKid.ProtoReflect.Descriptor instead.
func (*JwtRsaSsaPssPublicKey_CustomKid) Descriptor() ([]byte, []int) {
	return file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescGZIP(), []int{0, 0}
}

func (x *JwtRsaSsaPssPublicKey_CustomKid) GetValue() string {
	if x != nil {
		return x.Value
	}
	return ""
}

var File_third_party_tink_proto_jwt_rsa_ssa_pss_proto protoreflect.FileDescriptor

var file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDesc = []byte{
	0x0a, 0x2c, 0x74, 0x68, 0x69, 0x72, 0x64, 0x5f, 0x70, 0x61, 0x72, 0x74, 0x79, 0x2f, 0x74, 0x69,
	0x6e, 0x6b, 0x2f, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x2f, 0x6a, 0x77, 0x74, 0x5f, 0x72, 0x73, 0x61,
	0x5f, 0x73, 0x73, 0x61, 0x5f, 0x70, 0x73, 0x73, 0x2e, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x12, 0x12,
	0x67, 0x6f, 0x6f, 0x67, 0x6c, 0x65, 0x2e, 0x63, 0x72, 0x79, 0x70, 0x74, 0x6f, 0x2e, 0x74, 0x69,
	0x6e, 0x6b, 0x22, 0x8d, 0x02, 0x0a, 0x15, 0x4a, 0x77, 0x74, 0x52, 0x73, 0x61, 0x53, 0x73, 0x61,
	0x50, 0x73, 0x73, 0x50, 0x75, 0x62, 0x6c, 0x69, 0x63, 0x4b, 0x65, 0x79, 0x12, 0x18, 0x0a, 0x07,
	0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x18, 0x01, 0x20, 0x01, 0x28, 0x0d, 0x52, 0x07, 0x76,
	0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x12, 0x47, 0x0a, 0x09, 0x61, 0x6c, 0x67, 0x6f, 0x72, 0x69,
	0x74, 0x68, 0x6d, 0x18, 0x02, 0x20, 0x01, 0x28, 0x0e, 0x32, 0x29, 0x2e, 0x67, 0x6f, 0x6f, 0x67,
	0x6c, 0x65, 0x2e, 0x63, 0x72, 0x79, 0x70, 0x74, 0x6f, 0x2e, 0x74, 0x69, 0x6e, 0x6b, 0x2e, 0x4a,
	0x77, 0x74, 0x52, 0x73, 0x61, 0x53, 0x73, 0x61, 0x50, 0x73, 0x73, 0x41, 0x6c, 0x67, 0x6f, 0x72,
	0x69, 0x74, 0x68, 0x6d, 0x52, 0x09, 0x61, 0x6c, 0x67, 0x6f, 0x72, 0x69, 0x74, 0x68, 0x6d, 0x12,
	0x0c, 0x0a, 0x01, 0x6e, 0x18, 0x03, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x01, 0x6e, 0x12, 0x0c, 0x0a,
	0x01, 0x65, 0x18, 0x04, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x01, 0x65, 0x12, 0x52, 0x0a, 0x0a, 0x63,
	0x75, 0x73, 0x74, 0x6f, 0x6d, 0x5f, 0x6b, 0x69, 0x64, 0x18, 0x05, 0x20, 0x01, 0x28, 0x0b, 0x32,
	0x33, 0x2e, 0x67, 0x6f, 0x6f, 0x67, 0x6c, 0x65, 0x2e, 0x63, 0x72, 0x79, 0x70, 0x74, 0x6f, 0x2e,
	0x74, 0x69, 0x6e, 0x6b, 0x2e, 0x4a, 0x77, 0x74, 0x52, 0x73, 0x61, 0x53, 0x73, 0x61, 0x50, 0x73,
	0x73, 0x50, 0x75, 0x62, 0x6c, 0x69, 0x63, 0x4b, 0x65, 0x79, 0x2e, 0x43, 0x75, 0x73, 0x74, 0x6f,
	0x6d, 0x4b, 0x69, 0x64, 0x52, 0x09, 0x63, 0x75, 0x73, 0x74, 0x6f, 0x6d, 0x4b, 0x69, 0x64, 0x1a,
	0x21, 0x0a, 0x09, 0x43, 0x75, 0x73, 0x74, 0x6f, 0x6d, 0x4b, 0x69, 0x64, 0x12, 0x14, 0x0a, 0x05,
	0x76, 0x61, 0x6c, 0x75, 0x65, 0x18, 0x01, 0x20, 0x01, 0x28, 0x09, 0x52, 0x05, 0x76, 0x61, 0x6c,
	0x75, 0x65, 0x22, 0xd8, 0x01, 0x0a, 0x16, 0x4a, 0x77, 0x74, 0x52, 0x73, 0x61, 0x53, 0x73, 0x61,
	0x50, 0x73, 0x73, 0x50, 0x72, 0x69, 0x76, 0x61, 0x74, 0x65, 0x4b, 0x65, 0x79, 0x12, 0x18, 0x0a,
	0x07, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x18, 0x01, 0x20, 0x01, 0x28, 0x0d, 0x52, 0x07,
	0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x12, 0x48, 0x0a, 0x0a, 0x70, 0x75, 0x62, 0x6c, 0x69,
	0x63, 0x5f, 0x6b, 0x65, 0x79, 0x18, 0x02, 0x20, 0x01, 0x28, 0x0b, 0x32, 0x29, 0x2e, 0x67, 0x6f,
	0x6f, 0x67, 0x6c, 0x65, 0x2e, 0x63, 0x72, 0x79, 0x70, 0x74, 0x6f, 0x2e, 0x74, 0x69, 0x6e, 0x6b,
	0x2e, 0x4a, 0x77, 0x74, 0x52, 0x73, 0x61, 0x53, 0x73, 0x61, 0x50, 0x73, 0x73, 0x50, 0x75, 0x62,
	0x6c, 0x69, 0x63, 0x4b, 0x65, 0x79, 0x52, 0x09, 0x70, 0x75, 0x62, 0x6c, 0x69, 0x63, 0x4b, 0x65,
	0x79, 0x12, 0x0c, 0x0a, 0x01, 0x64, 0x18, 0x03, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x01, 0x64, 0x12,
	0x0c, 0x0a, 0x01, 0x70, 0x18, 0x04, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x01, 0x70, 0x12, 0x0c, 0x0a,
	0x01, 0x71, 0x18, 0x05, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x01, 0x71, 0x12, 0x0e, 0x0a, 0x02, 0x64,
	0x70, 0x18, 0x06, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x02, 0x64, 0x70, 0x12, 0x0e, 0x0a, 0x02, 0x64,
	0x71, 0x18, 0x07, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x02, 0x64, 0x71, 0x12, 0x10, 0x0a, 0x03, 0x63,
	0x72, 0x74, 0x18, 0x08, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x03, 0x63, 0x72, 0x74, 0x22, 0xd4, 0x01,
	0x0a, 0x15, 0x4a, 0x77, 0x74, 0x52, 0x73, 0x61, 0x53, 0x73, 0x61, 0x50, 0x73, 0x73, 0x4b, 0x65,
	0x79, 0x46, 0x6f, 0x72, 0x6d, 0x61, 0x74, 0x12, 0x18, 0x0a, 0x07, 0x76, 0x65, 0x72, 0x73, 0x69,
	0x6f, 0x6e, 0x18, 0x01, 0x20, 0x01, 0x28, 0x0d, 0x52, 0x07, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6f,
	0x6e, 0x12, 0x47, 0x0a, 0x09, 0x61, 0x6c, 0x67, 0x6f, 0x72, 0x69, 0x74, 0x68, 0x6d, 0x18, 0x02,
	0x20, 0x01, 0x28, 0x0e, 0x32, 0x29, 0x2e, 0x67, 0x6f, 0x6f, 0x67, 0x6c, 0x65, 0x2e, 0x63, 0x72,
	0x79, 0x70, 0x74, 0x6f, 0x2e, 0x74, 0x69, 0x6e, 0x6b, 0x2e, 0x4a, 0x77, 0x74, 0x52, 0x73, 0x61,
	0x53, 0x73, 0x61, 0x50, 0x73, 0x73, 0x41, 0x6c, 0x67, 0x6f, 0x72, 0x69, 0x74, 0x68, 0x6d, 0x52,
	0x09, 0x61, 0x6c, 0x67, 0x6f, 0x72, 0x69, 0x74, 0x68, 0x6d, 0x12, 0x2f, 0x0a, 0x14, 0x6d, 0x6f,
	0x64, 0x75, 0x6c, 0x75, 0x73, 0x5f, 0x73, 0x69, 0x7a, 0x65, 0x5f, 0x69, 0x6e, 0x5f, 0x62, 0x69,
	0x74, 0x73, 0x18, 0x03, 0x20, 0x01, 0x28, 0x0d, 0x52, 0x11, 0x6d, 0x6f, 0x64, 0x75, 0x6c, 0x75,
	0x73, 0x53, 0x69, 0x7a, 0x65, 0x49, 0x6e, 0x42, 0x69, 0x74, 0x73, 0x12, 0x27, 0x0a, 0x0f, 0x70,
	0x75, 0x62, 0x6c, 0x69, 0x63, 0x5f, 0x65, 0x78, 0x70, 0x6f, 0x6e, 0x65, 0x6e, 0x74, 0x18, 0x04,
	0x20, 0x01, 0x28, 0x0c, 0x52, 0x0e, 0x70, 0x75, 0x62, 0x6c, 0x69, 0x63, 0x45, 0x78, 0x70, 0x6f,
	0x6e, 0x65, 0x6e, 0x74, 0x2a, 0x48, 0x0a, 0x15, 0x4a, 0x77, 0x74, 0x52, 0x73, 0x61, 0x53, 0x73,
	0x61, 0x50, 0x73, 0x73, 0x41, 0x6c, 0x67, 0x6f, 0x72, 0x69, 0x74, 0x68, 0x6d, 0x12, 0x0e, 0x0a,
	0x0a, 0x50, 0x53, 0x5f, 0x55, 0x4e, 0x4b, 0x4e, 0x4f, 0x57, 0x4e, 0x10, 0x00, 0x12, 0x09, 0x0a,
	0x05, 0x50, 0x53, 0x32, 0x35, 0x36, 0x10, 0x01, 0x12, 0x09, 0x0a, 0x05, 0x50, 0x53, 0x33, 0x38,
	0x34, 0x10, 0x02, 0x12, 0x09, 0x0a, 0x05, 0x50, 0x53, 0x35, 0x31, 0x32, 0x10, 0x03, 0x42, 0x57,
	0x0a, 0x1c, 0x63, 0x6f, 0x6d, 0x2e, 0x67, 0x6f, 0x6f, 0x67, 0x6c, 0x65, 0x2e, 0x63, 0x72, 0x79,
	0x70, 0x74, 0x6f, 0x2e, 0x74, 0x69, 0x6e, 0x6b, 0x2e, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x50, 0x01,
	0x5a, 0x35, 0x67, 0x69, 0x74, 0x68, 0x75, 0x62, 0x2e, 0x63, 0x6f, 0x6d, 0x2f, 0x67, 0x6f, 0x6f,
	0x67, 0x6c, 0x65, 0x2f, 0x74, 0x69, 0x6e, 0x6b, 0x2f, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x2f, 0x6a,
	0x77, 0x74, 0x5f, 0x72, 0x73, 0x61, 0x5f, 0x73, 0x73, 0x61, 0x5f, 0x70, 0x73, 0x73, 0x5f, 0x67,
	0x6f, 0x5f, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x62, 0x06, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x33,
}

var (
	file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescOnce sync.Once
	file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescData = file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDesc
)

func file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescGZIP() []byte {
	file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescOnce.Do(func() {
		file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescData = protoimpl.X.CompressGZIP(file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescData)
	})
	return file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDescData
}

var file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_enumTypes = make([]protoimpl.EnumInfo, 1)
var file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes = make([]protoimpl.MessageInfo, 4)
var file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_goTypes = []interface{}{
	(JwtRsaSsaPssAlgorithm)(0),              // 0: google.crypto.tink.JwtRsaSsaPssAlgorithm
	(*JwtRsaSsaPssPublicKey)(nil),           // 1: google.crypto.tink.JwtRsaSsaPssPublicKey
	(*JwtRsaSsaPssPrivateKey)(nil),          // 2: google.crypto.tink.JwtRsaSsaPssPrivateKey
	(*JwtRsaSsaPssKeyFormat)(nil),           // 3: google.crypto.tink.JwtRsaSsaPssKeyFormat
	(*JwtRsaSsaPssPublicKey_CustomKid)(nil), // 4: google.crypto.tink.JwtRsaSsaPssPublicKey.CustomKid
}
var file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_depIdxs = []int32{
	0, // 0: google.crypto.tink.JwtRsaSsaPssPublicKey.algorithm:type_name -> google.crypto.tink.JwtRsaSsaPssAlgorithm
	4, // 1: google.crypto.tink.JwtRsaSsaPssPublicKey.custom_kid:type_name -> google.crypto.tink.JwtRsaSsaPssPublicKey.CustomKid
	1, // 2: google.crypto.tink.JwtRsaSsaPssPrivateKey.public_key:type_name -> google.crypto.tink.JwtRsaSsaPssPublicKey
	0, // 3: google.crypto.tink.JwtRsaSsaPssKeyFormat.algorithm:type_name -> google.crypto.tink.JwtRsaSsaPssAlgorithm
	4, // [4:4] is the sub-list for method output_type
	4, // [4:4] is the sub-list for method input_type
	4, // [4:4] is the sub-list for extension type_name
	4, // [4:4] is the sub-list for extension extendee
	0, // [0:4] is the sub-list for field type_name
}

func init() { file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_init() }
func file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_init() {
	if File_third_party_tink_proto_jwt_rsa_ssa_pss_proto != nil {
		return
	}
	if !protoimpl.UnsafeEnabled {
		file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[0].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*JwtRsaSsaPssPublicKey); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[1].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*JwtRsaSsaPssPrivateKey); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[2].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*JwtRsaSsaPssKeyFormat); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes[3].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*JwtRsaSsaPssPublicKey_CustomKid); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
	}
	type x struct{}
	out := protoimpl.TypeBuilder{
		File: protoimpl.DescBuilder{
			GoPackagePath: reflect.TypeOf(x{}).PkgPath(),
			RawDescriptor: file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDesc,
			NumEnums:      1,
			NumMessages:   4,
			NumExtensions: 0,
			NumServices:   0,
		},
		GoTypes:           file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_goTypes,
		DependencyIndexes: file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_depIdxs,
		EnumInfos:         file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_enumTypes,
		MessageInfos:      file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_msgTypes,
	}.Build()
	File_third_party_tink_proto_jwt_rsa_ssa_pss_proto = out.File
	file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_rawDesc = nil
	file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_goTypes = nil
	file_third_party_tink_proto_jwt_rsa_ssa_pss_proto_depIdxs = nil
}