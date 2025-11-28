package com.bbva.business.gateway.iso20022;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: iso20022.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CryptoServiceGrpc {

  private CryptoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "b.gateway.v1.CryptoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getTokenizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Tokenize",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getTokenizeMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getTokenizeMethod;
    if ((getTokenizeMethod = CryptoServiceGrpc.getTokenizeMethod) == null) {
      synchronized (CryptoServiceGrpc.class) {
        if ((getTokenizeMethod = CryptoServiceGrpc.getTokenizeMethod) == null) {
          CryptoServiceGrpc.getTokenizeMethod = getTokenizeMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Tokenize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new CryptoServiceMethodDescriptorSupplier("Tokenize"))
              .build();
        }
      }
    }
    return getTokenizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getUntokenizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Untokenize",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getUntokenizeMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getUntokenizeMethod;
    if ((getUntokenizeMethod = CryptoServiceGrpc.getUntokenizeMethod) == null) {
      synchronized (CryptoServiceGrpc.class) {
        if ((getUntokenizeMethod = CryptoServiceGrpc.getUntokenizeMethod) == null) {
          CryptoServiceGrpc.getUntokenizeMethod = getUntokenizeMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Untokenize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new CryptoServiceMethodDescriptorSupplier("Untokenize"))
              .build();
        }
      }
    }
    return getUntokenizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getValidateMacMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateMac",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getValidateMacMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getValidateMacMethod;
    if ((getValidateMacMethod = CryptoServiceGrpc.getValidateMacMethod) == null) {
      synchronized (CryptoServiceGrpc.class) {
        if ((getValidateMacMethod = CryptoServiceGrpc.getValidateMacMethod) == null) {
          CryptoServiceGrpc.getValidateMacMethod = getValidateMacMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateMac"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new CryptoServiceMethodDescriptorSupplier("ValidateMac"))
              .build();
        }
      }
    }
    return getValidateMacMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CryptoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CryptoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CryptoServiceStub>() {
        @java.lang.Override
        public CryptoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CryptoServiceStub(channel, callOptions);
        }
      };
    return CryptoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CryptoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CryptoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CryptoServiceBlockingStub>() {
        @java.lang.Override
        public CryptoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CryptoServiceBlockingStub(channel, callOptions);
        }
      };
    return CryptoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CryptoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CryptoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CryptoServiceFutureStub>() {
        @java.lang.Override
        public CryptoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CryptoServiceFutureStub(channel, callOptions);
        }
      };
    return CryptoServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void tokenize(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTokenizeMethod(), responseObserver);
    }

    /**
     */
    default void untokenize(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUntokenizeMethod(), responseObserver);
    }

    /**
     */
    default void validateMac(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateMacMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CryptoService.
   */
  public static abstract class CryptoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CryptoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CryptoService.
   */
  public static final class CryptoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CryptoServiceStub> {
    private CryptoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CryptoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CryptoServiceStub(channel, callOptions);
    }

    /**
     */
    public void tokenize(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTokenizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void untokenize(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUntokenizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateMac(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateMacMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CryptoService.
   */
  public static final class CryptoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CryptoServiceBlockingStub> {
    private CryptoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CryptoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CryptoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response tokenize(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTokenizeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response untokenize(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUntokenizeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response validateMac(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateMacMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CryptoService.
   */
  public static final class CryptoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CryptoServiceFutureStub> {
    private CryptoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CryptoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CryptoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> tokenize(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTokenizeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> untokenize(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUntokenizeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> validateMac(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateMacMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_TOKENIZE = 0;
  private static final int METHODID_UNTOKENIZE = 1;
  private static final int METHODID_VALIDATE_MAC = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_TOKENIZE:
          serviceImpl.tokenize((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response>) responseObserver);
          break;
        case METHODID_UNTOKENIZE:
          serviceImpl.untokenize((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response>) responseObserver);
          break;
        case METHODID_VALIDATE_MAC:
          serviceImpl.validateMac((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getTokenizeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_TOKENIZE)))
        .addMethod(
          getUntokenizeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_UNTOKENIZE)))
        .addMethod(
          getValidateMacMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_VALIDATE_MAC)))
        .build();
  }

  private static abstract class CryptoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CryptoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bbva.business.gateway.iso20022.Iso20022.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CryptoService");
    }
  }

  private static final class CryptoServiceFileDescriptorSupplier
      extends CryptoServiceBaseDescriptorSupplier {
    CryptoServiceFileDescriptorSupplier() {}
  }

  private static final class CryptoServiceMethodDescriptorSupplier
      extends CryptoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CryptoServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CryptoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CryptoServiceFileDescriptorSupplier())
              .addMethod(getTokenizeMethod())
              .addMethod(getUntokenizeMethod())
              .addMethod(getValidateMacMethod())
              .build();
        }
      }
    }
    return result;
  }
}
