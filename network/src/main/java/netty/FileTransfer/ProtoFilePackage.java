// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: FilePackage.proto

package netty.FileTransfer;

public final class ProtoFilePackage {
  private ProtoFilePackage() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface FilePackageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:FilePackage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string fileName = 1;</code>
     * @return The fileName.
     */
    java.lang.String getFileName();
    /**
     * <code>string fileName = 1;</code>
     * @return The bytes for fileName.
     */
    com.google.protobuf.ByteString
        getFileNameBytes();

    /**
     * <code>uint64 fileSize = 2;</code>
     * @return The fileSize.
     */
    long getFileSize();

    /**
     * <code>bytes contents = 3;</code>
     * @return The contents.
     */
    com.google.protobuf.ByteString getContents();

    /**
     * <code>string md5 = 4;</code>
     * @return The md5.
     */
    java.lang.String getMd5();
    /**
     * <code>string md5 = 4;</code>
     * @return The bytes for md5.
     */
    com.google.protobuf.ByteString
        getMd5Bytes();

    /**
     * <code>bool hasMoreSegments = 5;</code>
     * @return The hasMoreSegments.
     */
    boolean getHasMoreSegments();
  }
  /**
   * Protobuf type {@code FilePackage}
   */
  public static final class FilePackage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:FilePackage)
      FilePackageOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use FilePackage.newBuilder() to construct.
    private FilePackage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private FilePackage() {
      fileName_ = "";
      contents_ = com.google.protobuf.ByteString.EMPTY;
      md5_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new FilePackage();
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return netty.FileTransfer.ProtoFilePackage.internal_static_FilePackage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return netty.FileTransfer.ProtoFilePackage.internal_static_FilePackage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              netty.FileTransfer.ProtoFilePackage.FilePackage.class, netty.FileTransfer.ProtoFilePackage.FilePackage.Builder.class);
    }

    public static final int FILENAME_FIELD_NUMBER = 1;
    @SuppressWarnings("serial")
    private volatile java.lang.Object fileName_ = "";
    /**
     * <code>string fileName = 1;</code>
     * @return The fileName.
     */
    @java.lang.Override
    public java.lang.String getFileName() {
      java.lang.Object ref = fileName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        fileName_ = s;
        return s;
      }
    }
    /**
     * <code>string fileName = 1;</code>
     * @return The bytes for fileName.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getFileNameBytes() {
      java.lang.Object ref = fileName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        fileName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int FILESIZE_FIELD_NUMBER = 2;
    private long fileSize_ = 0L;
    /**
     * <code>uint64 fileSize = 2;</code>
     * @return The fileSize.
     */
    @java.lang.Override
    public long getFileSize() {
      return fileSize_;
    }

    public static final int CONTENTS_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString contents_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes contents = 3;</code>
     * @return The contents.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getContents() {
      return contents_;
    }

    public static final int MD5_FIELD_NUMBER = 4;
    @SuppressWarnings("serial")
    private volatile java.lang.Object md5_ = "";
    /**
     * <code>string md5 = 4;</code>
     * @return The md5.
     */
    @java.lang.Override
    public java.lang.String getMd5() {
      java.lang.Object ref = md5_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        md5_ = s;
        return s;
      }
    }
    /**
     * <code>string md5 = 4;</code>
     * @return The bytes for md5.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getMd5Bytes() {
      java.lang.Object ref = md5_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        md5_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int HASMORESEGMENTS_FIELD_NUMBER = 5;
    private boolean hasMoreSegments_ = false;
    /**
     * <code>bool hasMoreSegments = 5;</code>
     * @return The hasMoreSegments.
     */
    @java.lang.Override
    public boolean getHasMoreSegments() {
      return hasMoreSegments_;
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(fileName_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, fileName_);
      }
      if (fileSize_ != 0L) {
        output.writeUInt64(2, fileSize_);
      }
      if (!contents_.isEmpty()) {
        output.writeBytes(3, contents_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(md5_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, md5_);
      }
      if (hasMoreSegments_ != false) {
        output.writeBool(5, hasMoreSegments_);
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(fileName_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, fileName_);
      }
      if (fileSize_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(2, fileSize_);
      }
      if (!contents_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, contents_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(md5_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, md5_);
      }
      if (hasMoreSegments_ != false) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(5, hasMoreSegments_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof netty.FileTransfer.ProtoFilePackage.FilePackage)) {
        return super.equals(obj);
      }
      netty.FileTransfer.ProtoFilePackage.FilePackage other = (netty.FileTransfer.ProtoFilePackage.FilePackage) obj;

      if (!getFileName()
          .equals(other.getFileName())) return false;
      if (getFileSize()
          != other.getFileSize()) return false;
      if (!getContents()
          .equals(other.getContents())) return false;
      if (!getMd5()
          .equals(other.getMd5())) return false;
      if (getHasMoreSegments()
          != other.getHasMoreSegments()) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + FILENAME_FIELD_NUMBER;
      hash = (53 * hash) + getFileName().hashCode();
      hash = (37 * hash) + FILESIZE_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getFileSize());
      hash = (37 * hash) + CONTENTS_FIELD_NUMBER;
      hash = (53 * hash) + getContents().hashCode();
      hash = (37 * hash) + MD5_FIELD_NUMBER;
      hash = (53 * hash) + getMd5().hashCode();
      hash = (37 * hash) + HASMORESEGMENTS_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getHasMoreSegments());
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }

    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static netty.FileTransfer.ProtoFilePackage.FilePackage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(netty.FileTransfer.ProtoFilePackage.FilePackage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code FilePackage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:FilePackage)
        netty.FileTransfer.ProtoFilePackage.FilePackageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return netty.FileTransfer.ProtoFilePackage.internal_static_FilePackage_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return netty.FileTransfer.ProtoFilePackage.internal_static_FilePackage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                netty.FileTransfer.ProtoFilePackage.FilePackage.class, netty.FileTransfer.ProtoFilePackage.FilePackage.Builder.class);
      }

      // Construct using netty.FileTransfer.ProtoFilePackage.FilePackage.newBuilder()
      private Builder() {

      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);

      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        fileName_ = "";
        fileSize_ = 0L;
        contents_ = com.google.protobuf.ByteString.EMPTY;
        md5_ = "";
        hasMoreSegments_ = false;
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return netty.FileTransfer.ProtoFilePackage.internal_static_FilePackage_descriptor;
      }

      @java.lang.Override
      public netty.FileTransfer.ProtoFilePackage.FilePackage getDefaultInstanceForType() {
        return netty.FileTransfer.ProtoFilePackage.FilePackage.getDefaultInstance();
      }

      @java.lang.Override
      public netty.FileTransfer.ProtoFilePackage.FilePackage build() {
        netty.FileTransfer.ProtoFilePackage.FilePackage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public netty.FileTransfer.ProtoFilePackage.FilePackage buildPartial() {
        netty.FileTransfer.ProtoFilePackage.FilePackage result = new netty.FileTransfer.ProtoFilePackage.FilePackage(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(netty.FileTransfer.ProtoFilePackage.FilePackage result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.fileName_ = fileName_;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.fileSize_ = fileSize_;
        }
        if (((from_bitField0_ & 0x00000004) != 0)) {
          result.contents_ = contents_;
        }
        if (((from_bitField0_ & 0x00000008) != 0)) {
          result.md5_ = md5_;
        }
        if (((from_bitField0_ & 0x00000010) != 0)) {
          result.hasMoreSegments_ = hasMoreSegments_;
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof netty.FileTransfer.ProtoFilePackage.FilePackage) {
          return mergeFrom((netty.FileTransfer.ProtoFilePackage.FilePackage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(netty.FileTransfer.ProtoFilePackage.FilePackage other) {
        if (other == netty.FileTransfer.ProtoFilePackage.FilePackage.getDefaultInstance()) return this;
        if (!other.getFileName().isEmpty()) {
          fileName_ = other.fileName_;
          bitField0_ |= 0x00000001;
          onChanged();
        }
        if (other.getFileSize() != 0L) {
          setFileSize(other.getFileSize());
        }
        if (other.getContents() != com.google.protobuf.ByteString.EMPTY) {
          setContents(other.getContents());
        }
        if (!other.getMd5().isEmpty()) {
          md5_ = other.md5_;
          bitField0_ |= 0x00000008;
          onChanged();
        }
        if (other.getHasMoreSegments() != false) {
          setHasMoreSegments(other.getHasMoreSegments());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10: {
                fileName_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000001;
                break;
              } // case 10
              case 16: {
                fileSize_ = input.readUInt64();
                bitField0_ |= 0x00000002;
                break;
              } // case 16
              case 26: {
                contents_ = input.readBytes();
                bitField0_ |= 0x00000004;
                break;
              } // case 26
              case 34: {
                md5_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000008;
                break;
              } // case 34
              case 40: {
                hasMoreSegments_ = input.readBool();
                bitField0_ |= 0x00000010;
                break;
              } // case 40
              default: {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }
      private int bitField0_;

      private java.lang.Object fileName_ = "";
      /**
       * <code>string fileName = 1;</code>
       * @return The fileName.
       */
      public java.lang.String getFileName() {
        java.lang.Object ref = fileName_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          fileName_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string fileName = 1;</code>
       * @return The bytes for fileName.
       */
      public com.google.protobuf.ByteString
          getFileNameBytes() {
        java.lang.Object ref = fileName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          fileName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string fileName = 1;</code>
       * @param value The fileName to set.
       * @return This builder for chaining.
       */
      public Builder setFileName(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        fileName_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>string fileName = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearFileName() {
        fileName_ = getDefaultInstance().getFileName();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <code>string fileName = 1;</code>
       * @param value The bytes for fileName to set.
       * @return This builder for chaining.
       */
      public Builder setFileNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        fileName_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }

      private long fileSize_ ;
      /**
       * <code>uint64 fileSize = 2;</code>
       * @return The fileSize.
       */
      @java.lang.Override
      public long getFileSize() {
        return fileSize_;
      }
      /**
       * <code>uint64 fileSize = 2;</code>
       * @param value The fileSize to set.
       * @return This builder for chaining.
       */
      public Builder setFileSize(long value) {

        fileSize_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>uint64 fileSize = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearFileSize() {
        bitField0_ = (bitField0_ & ~0x00000002);
        fileSize_ = 0L;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString contents_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>bytes contents = 3;</code>
       * @return The contents.
       */
      @java.lang.Override
      public com.google.protobuf.ByteString getContents() {
        return contents_;
      }
      /**
       * <code>bytes contents = 3;</code>
       * @param value The contents to set.
       * @return This builder for chaining.
       */
      public Builder setContents(com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        contents_ = value;
        bitField0_ |= 0x00000004;
        onChanged();
        return this;
      }
      /**
       * <code>bytes contents = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearContents() {
        bitField0_ = (bitField0_ & ~0x00000004);
        contents_ = getDefaultInstance().getContents();
        onChanged();
        return this;
      }

      private java.lang.Object md5_ = "";
      /**
       * <code>string md5 = 4;</code>
       * @return The md5.
       */
      public java.lang.String getMd5() {
        java.lang.Object ref = md5_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          md5_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string md5 = 4;</code>
       * @return The bytes for md5.
       */
      public com.google.protobuf.ByteString
          getMd5Bytes() {
        java.lang.Object ref = md5_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          md5_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string md5 = 4;</code>
       * @param value The md5 to set.
       * @return This builder for chaining.
       */
      public Builder setMd5(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        md5_ = value;
        bitField0_ |= 0x00000008;
        onChanged();
        return this;
      }
      /**
       * <code>string md5 = 4;</code>
       * @return This builder for chaining.
       */
      public Builder clearMd5() {
        md5_ = getDefaultInstance().getMd5();
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
        return this;
      }
      /**
       * <code>string md5 = 4;</code>
       * @param value The bytes for md5 to set.
       * @return This builder for chaining.
       */
      public Builder setMd5Bytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        md5_ = value;
        bitField0_ |= 0x00000008;
        onChanged();
        return this;
      }

      private boolean hasMoreSegments_ ;
      /**
       * <code>bool hasMoreSegments = 5;</code>
       * @return The hasMoreSegments.
       */
      @java.lang.Override
      public boolean getHasMoreSegments() {
        return hasMoreSegments_;
      }
      /**
       * <code>bool hasMoreSegments = 5;</code>
       * @param value The hasMoreSegments to set.
       * @return This builder for chaining.
       */
      public Builder setHasMoreSegments(boolean value) {

        hasMoreSegments_ = value;
        bitField0_ |= 0x00000010;
        onChanged();
        return this;
      }
      /**
       * <code>bool hasMoreSegments = 5;</code>
       * @return This builder for chaining.
       */
      public Builder clearHasMoreSegments() {
        bitField0_ = (bitField0_ & ~0x00000010);
        hasMoreSegments_ = false;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:FilePackage)
    }

    // @@protoc_insertion_point(class_scope:FilePackage)
    private static final netty.FileTransfer.ProtoFilePackage.FilePackage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new netty.FileTransfer.ProtoFilePackage.FilePackage();
    }

    public static netty.FileTransfer.ProtoFilePackage.FilePackage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<FilePackage>
        PARSER = new com.google.protobuf.AbstractParser<FilePackage>() {
      @java.lang.Override
      public FilePackage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        Builder builder = newBuilder();
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (com.google.protobuf.UninitializedMessageException e) {
          throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(e)
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };

    public static com.google.protobuf.Parser<FilePackage> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<FilePackage> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public netty.FileTransfer.ProtoFilePackage.FilePackage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_FilePackage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_FilePackage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021FilePackage.proto\"i\n\013FilePackage\022\020\n\010fi" +
      "leName\030\001 \001(\t\022\020\n\010fileSize\030\002 \001(\004\022\020\n\010conten" +
      "ts\030\003 \001(\014\022\013\n\003md5\030\004 \001(\t\022\027\n\017hasMoreSegments" +
      "\030\005 \001(\010B(\n\022netty.FileTransferB\020ProtoFileP" +
      "ackageH\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_FilePackage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_FilePackage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_FilePackage_descriptor,
        new java.lang.String[] { "FileName", "FileSize", "Contents", "Md5", "HasMoreSegments", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}