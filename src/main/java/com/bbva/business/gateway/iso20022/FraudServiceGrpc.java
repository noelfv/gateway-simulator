package com.bbva.business.gateway.iso20022;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: iso20022.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FraudServiceGrpc {

  private FraudServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "b.gateway.v1.FraudService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getGetFraudInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetFraudInfo",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getGetFraudInfoMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getGetFraudInfoMethod;
    if ((getGetFraudInfoMethod = FraudServiceGrpc.getGetFraudInfoMethod) == null) {
      synchronized (FraudServiceGrpc.class) {
        if ((getGetFraudInfoMethod = FraudServiceGrpc.getGetFraudInfoMethod) == null) {
          FraudServiceGrpc.getGetFraudInfoMethod = getGetFraudInfoMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetFraudInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new FraudServiceMethodDescriptorSupplier("GetFraudInfo"))
              .build();
        }
      }
    }
    return getGetFraudInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Empty> getPostFeedBackFraudMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PostFeedBackFraud",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Empty> getPostFeedBackFraudMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Empty> getPostFeedBackFraudMethod;
    if ((getPostFeedBackFraudMethod = FraudServiceGrpc.getPostFeedBackFraudMethod) == null) {
      synchronized (FraudServiceGrpc.class) {
        if ((getPostFeedBackFraudMethod = FraudServiceGrpc.getPostFeedBackFraudMethod) == null) {
          FraudServiceGrpc.getPostFeedBackFraudMethod = getPostFeedBackFraudMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PostFeedBackFraud"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new FraudServiceMethodDescriptorSupplier("PostFeedBackFraud"))
              .build();
        }
      }
    }
    return getPostFeedBackFraudMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Empty> getGetFraudInfoAsyncMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetFraudInfoAsync",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Empty> getGetFraudInfoAsyncMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Empty> getGetFraudInfoAsyncMethod;
    if ((getGetFraudInfoAsyncMethod = FraudServiceGrpc.getGetFraudInfoAsyncMethod) == null) {
      synchronized (FraudServiceGrpc.class) {
        if ((getGetFraudInfoAsyncMethod = FraudServiceGrpc.getGetFraudInfoAsyncMethod) == null) {
          FraudServiceGrpc.getGetFraudInfoAsyncMethod = getGetFraudInfoAsyncMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetFraudInfoAsync"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new FraudServiceMethodDescriptorSupplier("GetFraudInfoAsync"))
              .build();
        }
      }
    }
    return getGetFraudInfoAsyncMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FraudServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FraudServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FraudServiceStub>() {
        @java.lang.Override
        public FraudServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FraudServiceStub(channel, callOptions);
        }
      };
    return FraudServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FraudServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FraudServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FraudServiceBlockingStub>() {
        @java.lang.Override
        public FraudServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FraudServiceBlockingStub(channel, callOptions);
        }
      };
    return FraudServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FraudServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FraudServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FraudServiceFutureStub>() {
        @java.lang.Override
        public FraudServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FraudServiceFutureStub(channel, callOptions);
        }
      };
    return FraudServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getFraudInfo(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetFraudInfoMethod(), responseObserver);
    }

    /**
     */
    default void postFeedBackFraud(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPostFeedBackFraudMethod(), responseObserver);
    }

    /**
     */
    default void getFraudInfoAsync(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetFraudInfoAsyncMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FraudService.
   */
  public static abstract class FraudServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FraudServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FraudService.
   */
  public static final class FraudServiceStub
      extends io.grpc.stub.AbstractAsyncStub<FraudServiceStub> {
    private FraudServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FraudServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FraudServiceStub(channel, callOptions);
    }

    /**
     */
    public void getFraudInfo(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetFraudInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void postFeedBackFraud(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPostFeedBackFraudMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getFraudInfoAsync(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetFraudInfoAsyncMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FraudService.
   */
  public static final class FraudServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FraudServiceBlockingStub> {
    private FraudServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FraudServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FraudServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response getFraudInfo(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetFraudInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Empty postFeedBackFraud(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPostFeedBackFraudMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Empty getFraudInfoAsync(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetFraudInfoAsyncMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FraudService.
   */
  public static final class FraudServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<FraudServiceFutureStub> {
    private FraudServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FraudServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FraudServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> getFraudInfo(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetFraudInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Empty> postFeedBackFraud(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPostFeedBackFraudMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Empty> getFraudInfoAsync(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetFraudInfoAsyncMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_FRAUD_INFO = 0;
  private static final int METHODID_POST_FEED_BACK_FRAUD = 1;
  private static final int METHODID_GET_FRAUD_INFO_ASYNC = 2;

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
        case METHODID_GET_FRAUD_INFO:
          serviceImpl.getFraudInfo((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response>) responseObserver);
          break;
        case METHODID_POST_FEED_BACK_FRAUD:
          serviceImpl.postFeedBackFraud((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Empty>) responseObserver);
          break;
        case METHODID_GET_FRAUD_INFO_ASYNC:
          serviceImpl.getFraudInfoAsync((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Empty>) responseObserver);
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
          getGetFraudInfoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_GET_FRAUD_INFO)))
        .addMethod(
          getPostFeedBackFraudMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Empty>(
                service, METHODID_POST_FEED_BACK_FRAUD)))
        .addMethod(
          getGetFraudInfoAsyncMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Empty>(
                service, METHODID_GET_FRAUD_INFO_ASYNC)))
        .build();
  }

  private static abstract class FraudServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FraudServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bbva.business.gateway.iso20022.Iso20022.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FraudService");
    }
  }

  private static final class FraudServiceFileDescriptorSupplier
      extends FraudServiceBaseDescriptorSupplier {
    FraudServiceFileDescriptorSupplier() {}
  }

  private static final class FraudServiceMethodDescriptorSupplier
      extends FraudServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FraudServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (FraudServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FraudServiceFileDescriptorSupplier())
              .addMethod(getGetFraudInfoMethod())
              .addMethod(getPostFeedBackFraudMethod())
              .addMethod(getGetFraudInfoAsyncMethod())
              .build();
        }
      }
    }
    return result;
  }
}
