package com.bbva.business.gateway.orchestrator;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: orchestrator.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class OrchestratorServiceGrpc {

  private OrchestratorServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "b.gateway.v1.OrchestratorService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.orchestrator.PostProcessMessageRequest,
      com.bbva.business.gateway.orchestrator.PostProcessMessageResponse> getPostProcessMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PostProcessMessage",
      requestType = com.bbva.business.gateway.orchestrator.PostProcessMessageRequest.class,
      responseType = com.bbva.business.gateway.orchestrator.PostProcessMessageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.orchestrator.PostProcessMessageRequest,
      com.bbva.business.gateway.orchestrator.PostProcessMessageResponse> getPostProcessMessageMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.orchestrator.PostProcessMessageRequest, com.bbva.business.gateway.orchestrator.PostProcessMessageResponse> getPostProcessMessageMethod;
    if ((getPostProcessMessageMethod = OrchestratorServiceGrpc.getPostProcessMessageMethod) == null) {
      synchronized (OrchestratorServiceGrpc.class) {
        if ((getPostProcessMessageMethod = OrchestratorServiceGrpc.getPostProcessMessageMethod) == null) {
          OrchestratorServiceGrpc.getPostProcessMessageMethod = getPostProcessMessageMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.orchestrator.PostProcessMessageRequest, com.bbva.business.gateway.orchestrator.PostProcessMessageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PostProcessMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.orchestrator.PostProcessMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.orchestrator.PostProcessMessageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrchestratorServiceMethodDescriptorSupplier("PostProcessMessage"))
              .build();
        }
      }
    }
    return getPostProcessMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.orchestrator.PostProcessMessageRequest,
      com.bbva.business.gateway.orchestrator.Empty> getPostProcessMessageAsyncMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PostProcessMessageAsync",
      requestType = com.bbva.business.gateway.orchestrator.PostProcessMessageRequest.class,
      responseType = com.bbva.business.gateway.orchestrator.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.orchestrator.PostProcessMessageRequest,
      com.bbva.business.gateway.orchestrator.Empty> getPostProcessMessageAsyncMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.orchestrator.PostProcessMessageRequest, com.bbva.business.gateway.orchestrator.Empty> getPostProcessMessageAsyncMethod;
    if ((getPostProcessMessageAsyncMethod = OrchestratorServiceGrpc.getPostProcessMessageAsyncMethod) == null) {
      synchronized (OrchestratorServiceGrpc.class) {
        if ((getPostProcessMessageAsyncMethod = OrchestratorServiceGrpc.getPostProcessMessageAsyncMethod) == null) {
          OrchestratorServiceGrpc.getPostProcessMessageAsyncMethod = getPostProcessMessageAsyncMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.orchestrator.PostProcessMessageRequest, com.bbva.business.gateway.orchestrator.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PostProcessMessageAsync"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.orchestrator.PostProcessMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.orchestrator.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new OrchestratorServiceMethodDescriptorSupplier("PostProcessMessageAsync"))
              .build();
        }
      }
    }
    return getPostProcessMessageAsyncMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OrchestratorServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrchestratorServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrchestratorServiceStub>() {
        @java.lang.Override
        public OrchestratorServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrchestratorServiceStub(channel, callOptions);
        }
      };
    return OrchestratorServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OrchestratorServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrchestratorServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrchestratorServiceBlockingStub>() {
        @java.lang.Override
        public OrchestratorServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrchestratorServiceBlockingStub(channel, callOptions);
        }
      };
    return OrchestratorServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OrchestratorServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrchestratorServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrchestratorServiceFutureStub>() {
        @java.lang.Override
        public OrchestratorServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrchestratorServiceFutureStub(channel, callOptions);
        }
      };
    return OrchestratorServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void postProcessMessage(com.bbva.business.gateway.orchestrator.PostProcessMessageRequest request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.orchestrator.PostProcessMessageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPostProcessMessageMethod(), responseObserver);
    }

    /**
     */
    default void postProcessMessageAsync(com.bbva.business.gateway.orchestrator.PostProcessMessageRequest request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.orchestrator.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPostProcessMessageAsyncMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service OrchestratorService.
   */
  public static abstract class OrchestratorServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return OrchestratorServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service OrchestratorService.
   */
  public static final class OrchestratorServiceStub
      extends io.grpc.stub.AbstractAsyncStub<OrchestratorServiceStub> {
    private OrchestratorServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrchestratorServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrchestratorServiceStub(channel, callOptions);
    }

    /**
     */
    public void postProcessMessage(com.bbva.business.gateway.orchestrator.PostProcessMessageRequest request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.orchestrator.PostProcessMessageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPostProcessMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void postProcessMessageAsync(com.bbva.business.gateway.orchestrator.PostProcessMessageRequest request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.orchestrator.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPostProcessMessageAsyncMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service OrchestratorService.
   */
  public static final class OrchestratorServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<OrchestratorServiceBlockingStub> {
    private OrchestratorServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrchestratorServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrchestratorServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bbva.business.gateway.orchestrator.PostProcessMessageResponse postProcessMessage(com.bbva.business.gateway.orchestrator.PostProcessMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPostProcessMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.orchestrator.Empty postProcessMessageAsync(com.bbva.business.gateway.orchestrator.PostProcessMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPostProcessMessageAsyncMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service OrchestratorService.
   */
  public static final class OrchestratorServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<OrchestratorServiceFutureStub> {
    private OrchestratorServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrchestratorServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrchestratorServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.orchestrator.PostProcessMessageResponse> postProcessMessage(
        com.bbva.business.gateway.orchestrator.PostProcessMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPostProcessMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.orchestrator.Empty> postProcessMessageAsync(
        com.bbva.business.gateway.orchestrator.PostProcessMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPostProcessMessageAsyncMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_POST_PROCESS_MESSAGE = 0;
  private static final int METHODID_POST_PROCESS_MESSAGE_ASYNC = 1;

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
        case METHODID_POST_PROCESS_MESSAGE:
          serviceImpl.postProcessMessage((com.bbva.business.gateway.orchestrator.PostProcessMessageRequest) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.orchestrator.PostProcessMessageResponse>) responseObserver);
          break;
        case METHODID_POST_PROCESS_MESSAGE_ASYNC:
          serviceImpl.postProcessMessageAsync((com.bbva.business.gateway.orchestrator.PostProcessMessageRequest) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.orchestrator.Empty>) responseObserver);
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
          getPostProcessMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.orchestrator.PostProcessMessageRequest,
              com.bbva.business.gateway.orchestrator.PostProcessMessageResponse>(
                service, METHODID_POST_PROCESS_MESSAGE)))
        .addMethod(
          getPostProcessMessageAsyncMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.orchestrator.PostProcessMessageRequest,
              com.bbva.business.gateway.orchestrator.Empty>(
                service, METHODID_POST_PROCESS_MESSAGE_ASYNC)))
        .build();
  }

  private static abstract class OrchestratorServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OrchestratorServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bbva.business.gateway.orchestrator.Orchestrator.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OrchestratorService");
    }
  }

  private static final class OrchestratorServiceFileDescriptorSupplier
      extends OrchestratorServiceBaseDescriptorSupplier {
    OrchestratorServiceFileDescriptorSupplier() {}
  }

  private static final class OrchestratorServiceMethodDescriptorSupplier
      extends OrchestratorServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    OrchestratorServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (OrchestratorServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OrchestratorServiceFileDescriptorSupplier())
              .addMethod(getPostProcessMessageMethod())
              .addMethod(getPostProcessMessageAsyncMethod())
              .build();
        }
      }
    }
    return result;
  }
}
