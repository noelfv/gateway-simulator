package com.bbva.business.gateway.iso20022;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: iso20022.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FlowHandlerServiceGrpc {

  private FlowHandlerServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "b.gateway.v1.FlowHandlerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getSendResolverConnectorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendResolverConnector",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getSendResolverConnectorMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getSendResolverConnectorMethod;
    if ((getSendResolverConnectorMethod = FlowHandlerServiceGrpc.getSendResolverConnectorMethod) == null) {
      synchronized (FlowHandlerServiceGrpc.class) {
        if ((getSendResolverConnectorMethod = FlowHandlerServiceGrpc.getSendResolverConnectorMethod) == null) {
          FlowHandlerServiceGrpc.getSendResolverConnectorMethod = getSendResolverConnectorMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendResolverConnector"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new FlowHandlerServiceMethodDescriptorSupplier("SendResolverConnector"))
              .build();
        }
      }
    }
    return getSendResolverConnectorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Empty> getSendResolverConnectorAsyncMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendResolverConnectorAsync",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Empty> getSendResolverConnectorAsyncMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Empty> getSendResolverConnectorAsyncMethod;
    if ((getSendResolverConnectorAsyncMethod = FlowHandlerServiceGrpc.getSendResolverConnectorAsyncMethod) == null) {
      synchronized (FlowHandlerServiceGrpc.class) {
        if ((getSendResolverConnectorAsyncMethod = FlowHandlerServiceGrpc.getSendResolverConnectorAsyncMethod) == null) {
          FlowHandlerServiceGrpc.getSendResolverConnectorAsyncMethod = getSendResolverConnectorAsyncMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendResolverConnectorAsync"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new FlowHandlerServiceMethodDescriptorSupplier("SendResolverConnectorAsync"))
              .build();
        }
      }
    }
    return getSendResolverConnectorAsyncMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FlowHandlerServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FlowHandlerServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FlowHandlerServiceStub>() {
        @java.lang.Override
        public FlowHandlerServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FlowHandlerServiceStub(channel, callOptions);
        }
      };
    return FlowHandlerServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FlowHandlerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FlowHandlerServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FlowHandlerServiceBlockingStub>() {
        @java.lang.Override
        public FlowHandlerServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FlowHandlerServiceBlockingStub(channel, callOptions);
        }
      };
    return FlowHandlerServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FlowHandlerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FlowHandlerServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FlowHandlerServiceFutureStub>() {
        @java.lang.Override
        public FlowHandlerServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FlowHandlerServiceFutureStub(channel, callOptions);
        }
      };
    return FlowHandlerServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void sendResolverConnector(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendResolverConnectorMethod(), responseObserver);
    }

    /**
     */
    default void sendResolverConnectorAsync(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendResolverConnectorAsyncMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FlowHandlerService.
   */
  public static abstract class FlowHandlerServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FlowHandlerServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FlowHandlerService.
   */
  public static final class FlowHandlerServiceStub
      extends io.grpc.stub.AbstractAsyncStub<FlowHandlerServiceStub> {
    private FlowHandlerServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FlowHandlerServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FlowHandlerServiceStub(channel, callOptions);
    }

    /**
     */
    public void sendResolverConnector(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendResolverConnectorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendResolverConnectorAsync(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendResolverConnectorAsyncMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FlowHandlerService.
   */
  public static final class FlowHandlerServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FlowHandlerServiceBlockingStub> {
    private FlowHandlerServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FlowHandlerServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FlowHandlerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response sendResolverConnector(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendResolverConnectorMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Empty sendResolverConnectorAsync(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendResolverConnectorAsyncMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FlowHandlerService.
   */
  public static final class FlowHandlerServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<FlowHandlerServiceFutureStub> {
    private FlowHandlerServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FlowHandlerServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FlowHandlerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> sendResolverConnector(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendResolverConnectorMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Empty> sendResolverConnectorAsync(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendResolverConnectorAsyncMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_RESOLVER_CONNECTOR = 0;
  private static final int METHODID_SEND_RESOLVER_CONNECTOR_ASYNC = 1;

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
        case METHODID_SEND_RESOLVER_CONNECTOR:
          serviceImpl.sendResolverConnector((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response>) responseObserver);
          break;
        case METHODID_SEND_RESOLVER_CONNECTOR_ASYNC:
          serviceImpl.sendResolverConnectorAsync((com.bbva.business.gateway.iso20022.Iso20022Request) request,
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
          getSendResolverConnectorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_SEND_RESOLVER_CONNECTOR)))
        .addMethod(
          getSendResolverConnectorAsyncMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Empty>(
                service, METHODID_SEND_RESOLVER_CONNECTOR_ASYNC)))
        .build();
  }

  private static abstract class FlowHandlerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FlowHandlerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bbva.business.gateway.iso20022.Iso20022.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FlowHandlerService");
    }
  }

  private static final class FlowHandlerServiceFileDescriptorSupplier
      extends FlowHandlerServiceBaseDescriptorSupplier {
    FlowHandlerServiceFileDescriptorSupplier() {}
  }

  private static final class FlowHandlerServiceMethodDescriptorSupplier
      extends FlowHandlerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FlowHandlerServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (FlowHandlerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FlowHandlerServiceFileDescriptorSupplier())
              .addMethod(getSendResolverConnectorMethod())
              .addMethod(getSendResolverConnectorAsyncMethod())
              .build();
        }
      }
    }
    return result;
  }
}
