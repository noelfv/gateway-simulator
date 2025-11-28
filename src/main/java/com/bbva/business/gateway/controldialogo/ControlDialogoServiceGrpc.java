package com.bbva.business.gateway.controldialogo;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: controlDialogo.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ControlDialogoServiceGrpc {

  private ControlDialogoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "b.gateway.v1.ControlDialogoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.controldialogo.Iso20022ControlRequest,
      com.bbva.business.gateway.controldialogo.Iso20022ControlResponse> getControlDialogoProcessMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ControlDialogoProcessMessage",
      requestType = com.bbva.business.gateway.controldialogo.Iso20022ControlRequest.class,
      responseType = com.bbva.business.gateway.controldialogo.Iso20022ControlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.controldialogo.Iso20022ControlRequest,
      com.bbva.business.gateway.controldialogo.Iso20022ControlResponse> getControlDialogoProcessMessageMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.controldialogo.Iso20022ControlRequest, com.bbva.business.gateway.controldialogo.Iso20022ControlResponse> getControlDialogoProcessMessageMethod;
    if ((getControlDialogoProcessMessageMethod = ControlDialogoServiceGrpc.getControlDialogoProcessMessageMethod) == null) {
      synchronized (ControlDialogoServiceGrpc.class) {
        if ((getControlDialogoProcessMessageMethod = ControlDialogoServiceGrpc.getControlDialogoProcessMessageMethod) == null) {
          ControlDialogoServiceGrpc.getControlDialogoProcessMessageMethod = getControlDialogoProcessMessageMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.controldialogo.Iso20022ControlRequest, com.bbva.business.gateway.controldialogo.Iso20022ControlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ControlDialogoProcessMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.controldialogo.Iso20022ControlRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.controldialogo.Iso20022ControlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ControlDialogoServiceMethodDescriptorSupplier("ControlDialogoProcessMessage"))
              .build();
        }
      }
    }
    return getControlDialogoProcessMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ControlDialogoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ControlDialogoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ControlDialogoServiceStub>() {
        @java.lang.Override
        public ControlDialogoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ControlDialogoServiceStub(channel, callOptions);
        }
      };
    return ControlDialogoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ControlDialogoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ControlDialogoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ControlDialogoServiceBlockingStub>() {
        @java.lang.Override
        public ControlDialogoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ControlDialogoServiceBlockingStub(channel, callOptions);
        }
      };
    return ControlDialogoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ControlDialogoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ControlDialogoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ControlDialogoServiceFutureStub>() {
        @java.lang.Override
        public ControlDialogoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ControlDialogoServiceFutureStub(channel, callOptions);
        }
      };
    return ControlDialogoServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void controlDialogoProcessMessage(com.bbva.business.gateway.controldialogo.Iso20022ControlRequest request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.controldialogo.Iso20022ControlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getControlDialogoProcessMessageMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ControlDialogoService.
   */
  public static abstract class ControlDialogoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ControlDialogoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ControlDialogoService.
   */
  public static final class ControlDialogoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ControlDialogoServiceStub> {
    private ControlDialogoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlDialogoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ControlDialogoServiceStub(channel, callOptions);
    }

    /**
     */
    public void controlDialogoProcessMessage(com.bbva.business.gateway.controldialogo.Iso20022ControlRequest request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.controldialogo.Iso20022ControlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getControlDialogoProcessMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ControlDialogoService.
   */
  public static final class ControlDialogoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ControlDialogoServiceBlockingStub> {
    private ControlDialogoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlDialogoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ControlDialogoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bbva.business.gateway.controldialogo.Iso20022ControlResponse controlDialogoProcessMessage(com.bbva.business.gateway.controldialogo.Iso20022ControlRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getControlDialogoProcessMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ControlDialogoService.
   */
  public static final class ControlDialogoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ControlDialogoServiceFutureStub> {
    private ControlDialogoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlDialogoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ControlDialogoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.controldialogo.Iso20022ControlResponse> controlDialogoProcessMessage(
        com.bbva.business.gateway.controldialogo.Iso20022ControlRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getControlDialogoProcessMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CONTROL_DIALOGO_PROCESS_MESSAGE = 0;

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
        case METHODID_CONTROL_DIALOGO_PROCESS_MESSAGE:
          serviceImpl.controlDialogoProcessMessage((com.bbva.business.gateway.controldialogo.Iso20022ControlRequest) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.controldialogo.Iso20022ControlResponse>) responseObserver);
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
          getControlDialogoProcessMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.controldialogo.Iso20022ControlRequest,
              com.bbva.business.gateway.controldialogo.Iso20022ControlResponse>(
                service, METHODID_CONTROL_DIALOGO_PROCESS_MESSAGE)))
        .build();
  }

  private static abstract class ControlDialogoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ControlDialogoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bbva.business.gateway.controldialogo.ControlDialogo.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ControlDialogoService");
    }
  }

  private static final class ControlDialogoServiceFileDescriptorSupplier
      extends ControlDialogoServiceBaseDescriptorSupplier {
    ControlDialogoServiceFileDescriptorSupplier() {}
  }

  private static final class ControlDialogoServiceMethodDescriptorSupplier
      extends ControlDialogoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ControlDialogoServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ControlDialogoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ControlDialogoServiceFileDescriptorSupplier())
              .addMethod(getControlDialogoProcessMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
