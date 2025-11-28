package com.bbva.business.gateway.iso20022;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: iso20022.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DummyResponseServiceGrpc {

  private DummyResponseServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "b.gateway.v1.DummyResponseService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getGetDummyResponseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDummyResponse",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getGetDummyResponseMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getGetDummyResponseMethod;
    if ((getGetDummyResponseMethod = DummyResponseServiceGrpc.getGetDummyResponseMethod) == null) {
      synchronized (DummyResponseServiceGrpc.class) {
        if ((getGetDummyResponseMethod = DummyResponseServiceGrpc.getGetDummyResponseMethod) == null) {
          DummyResponseServiceGrpc.getGetDummyResponseMethod = getGetDummyResponseMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDummyResponse"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new DummyResponseServiceMethodDescriptorSupplier("GetDummyResponse"))
              .build();
        }
      }
    }
    return getGetDummyResponseMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DummyResponseServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DummyResponseServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DummyResponseServiceStub>() {
        @java.lang.Override
        public DummyResponseServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DummyResponseServiceStub(channel, callOptions);
        }
      };
    return DummyResponseServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DummyResponseServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DummyResponseServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DummyResponseServiceBlockingStub>() {
        @java.lang.Override
        public DummyResponseServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DummyResponseServiceBlockingStub(channel, callOptions);
        }
      };
    return DummyResponseServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DummyResponseServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DummyResponseServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DummyResponseServiceFutureStub>() {
        @java.lang.Override
        public DummyResponseServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DummyResponseServiceFutureStub(channel, callOptions);
        }
      };
    return DummyResponseServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getDummyResponse(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDummyResponseMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DummyResponseService.
   */
  public static abstract class DummyResponseServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DummyResponseServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DummyResponseService.
   */
  public static final class DummyResponseServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DummyResponseServiceStub> {
    private DummyResponseServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DummyResponseServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DummyResponseServiceStub(channel, callOptions);
    }

    /**
     */
    public void getDummyResponse(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDummyResponseMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DummyResponseService.
   */
  public static final class DummyResponseServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DummyResponseServiceBlockingStub> {
    private DummyResponseServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DummyResponseServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DummyResponseServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response getDummyResponse(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDummyResponseMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DummyResponseService.
   */
  public static final class DummyResponseServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DummyResponseServiceFutureStub> {
    private DummyResponseServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DummyResponseServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DummyResponseServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> getDummyResponse(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDummyResponseMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_DUMMY_RESPONSE = 0;

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
        case METHODID_GET_DUMMY_RESPONSE:
          serviceImpl.getDummyResponse((com.bbva.business.gateway.iso20022.Iso20022Request) request,
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
          getGetDummyResponseMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_GET_DUMMY_RESPONSE)))
        .build();
  }

  private static abstract class DummyResponseServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DummyResponseServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bbva.business.gateway.iso20022.Iso20022.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DummyResponseService");
    }
  }

  private static final class DummyResponseServiceFileDescriptorSupplier
      extends DummyResponseServiceBaseDescriptorSupplier {
    DummyResponseServiceFileDescriptorSupplier() {}
  }

  private static final class DummyResponseServiceMethodDescriptorSupplier
      extends DummyResponseServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DummyResponseServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DummyResponseServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DummyResponseServiceFileDescriptorSupplier())
              .addMethod(getGetDummyResponseMethod())
              .build();
        }
      }
    }
    return result;
  }
}
