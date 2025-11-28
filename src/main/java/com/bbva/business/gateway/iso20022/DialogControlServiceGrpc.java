package com.bbva.business.gateway.iso20022;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: iso20022.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DialogControlServiceGrpc {

  private DialogControlServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "b.gateway.v1.DialogControlService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getProcessMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Process",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getProcessMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getProcessMethod;
    if ((getProcessMethod = DialogControlServiceGrpc.getProcessMethod) == null) {
      synchronized (DialogControlServiceGrpc.class) {
        if ((getProcessMethod = DialogControlServiceGrpc.getProcessMethod) == null) {
          DialogControlServiceGrpc.getProcessMethod = getProcessMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Process"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new DialogControlServiceMethodDescriptorSupplier("Process"))
              .build();
        }
      }
    }
    return getProcessMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getSignOnMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SignOn",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getSignOnMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getSignOnMethod;
    if ((getSignOnMethod = DialogControlServiceGrpc.getSignOnMethod) == null) {
      synchronized (DialogControlServiceGrpc.class) {
        if ((getSignOnMethod = DialogControlServiceGrpc.getSignOnMethod) == null) {
          DialogControlServiceGrpc.getSignOnMethod = getSignOnMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SignOn"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new DialogControlServiceMethodDescriptorSupplier("SignOn"))
              .build();
        }
      }
    }
    return getSignOnMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getSignOffMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SignOff",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getSignOffMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getSignOffMethod;
    if ((getSignOffMethod = DialogControlServiceGrpc.getSignOffMethod) == null) {
      synchronized (DialogControlServiceGrpc.class) {
        if ((getSignOffMethod = DialogControlServiceGrpc.getSignOffMethod) == null) {
          DialogControlServiceGrpc.getSignOffMethod = getSignOffMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SignOff"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new DialogControlServiceMethodDescriptorSupplier("SignOff"))
              .build();
        }
      }
    }
    return getSignOffMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getEchoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Echo",
      requestType = com.bbva.business.gateway.iso20022.Iso20022Request.class,
      responseType = com.bbva.business.gateway.iso20022.Iso20022Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request,
      com.bbva.business.gateway.iso20022.Iso20022Response> getEchoMethod() {
    io.grpc.MethodDescriptor<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response> getEchoMethod;
    if ((getEchoMethod = DialogControlServiceGrpc.getEchoMethod) == null) {
      synchronized (DialogControlServiceGrpc.class) {
        if ((getEchoMethod = DialogControlServiceGrpc.getEchoMethod) == null) {
          DialogControlServiceGrpc.getEchoMethod = getEchoMethod =
              io.grpc.MethodDescriptor.<com.bbva.business.gateway.iso20022.Iso20022Request, com.bbva.business.gateway.iso20022.Iso20022Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Echo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bbva.business.gateway.iso20022.Iso20022Response.getDefaultInstance()))
              .setSchemaDescriptor(new DialogControlServiceMethodDescriptorSupplier("Echo"))
              .build();
        }
      }
    }
    return getEchoMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DialogControlServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DialogControlServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DialogControlServiceStub>() {
        @java.lang.Override
        public DialogControlServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DialogControlServiceStub(channel, callOptions);
        }
      };
    return DialogControlServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DialogControlServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DialogControlServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DialogControlServiceBlockingStub>() {
        @java.lang.Override
        public DialogControlServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DialogControlServiceBlockingStub(channel, callOptions);
        }
      };
    return DialogControlServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DialogControlServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DialogControlServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DialogControlServiceFutureStub>() {
        @java.lang.Override
        public DialogControlServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DialogControlServiceFutureStub(channel, callOptions);
        }
      };
    return DialogControlServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void process(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getProcessMethod(), responseObserver);
    }

    /**
     */
    default void signOn(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSignOnMethod(), responseObserver);
    }

    /**
     */
    default void signOff(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSignOffMethod(), responseObserver);
    }

    /**
     */
    default void echo(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEchoMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DialogControlService.
   */
  public static abstract class DialogControlServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DialogControlServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DialogControlService.
   */
  public static final class DialogControlServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DialogControlServiceStub> {
    private DialogControlServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DialogControlServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DialogControlServiceStub(channel, callOptions);
    }

    /**
     */
    public void process(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getProcessMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void signOn(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSignOnMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void signOff(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSignOffMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void echo(com.bbva.business.gateway.iso20022.Iso20022Request request,
        io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEchoMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DialogControlService.
   */
  public static final class DialogControlServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DialogControlServiceBlockingStub> {
    private DialogControlServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DialogControlServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DialogControlServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response process(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getProcessMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response signOn(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSignOnMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response signOff(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSignOffMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bbva.business.gateway.iso20022.Iso20022Response echo(com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEchoMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DialogControlService.
   */
  public static final class DialogControlServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DialogControlServiceFutureStub> {
    private DialogControlServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DialogControlServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DialogControlServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> process(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getProcessMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> signOn(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSignOnMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> signOff(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSignOffMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bbva.business.gateway.iso20022.Iso20022Response> echo(
        com.bbva.business.gateway.iso20022.Iso20022Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEchoMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PROCESS = 0;
  private static final int METHODID_SIGN_ON = 1;
  private static final int METHODID_SIGN_OFF = 2;
  private static final int METHODID_ECHO = 3;

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
        case METHODID_PROCESS:
          serviceImpl.process((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response>) responseObserver);
          break;
        case METHODID_SIGN_ON:
          serviceImpl.signOn((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response>) responseObserver);
          break;
        case METHODID_SIGN_OFF:
          serviceImpl.signOff((com.bbva.business.gateway.iso20022.Iso20022Request) request,
              (io.grpc.stub.StreamObserver<com.bbva.business.gateway.iso20022.Iso20022Response>) responseObserver);
          break;
        case METHODID_ECHO:
          serviceImpl.echo((com.bbva.business.gateway.iso20022.Iso20022Request) request,
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
          getProcessMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_PROCESS)))
        .addMethod(
          getSignOnMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_SIGN_ON)))
        .addMethod(
          getSignOffMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_SIGN_OFF)))
        .addMethod(
          getEchoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bbva.business.gateway.iso20022.Iso20022Request,
              com.bbva.business.gateway.iso20022.Iso20022Response>(
                service, METHODID_ECHO)))
        .build();
  }

  private static abstract class DialogControlServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DialogControlServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bbva.business.gateway.iso20022.Iso20022.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DialogControlService");
    }
  }

  private static final class DialogControlServiceFileDescriptorSupplier
      extends DialogControlServiceBaseDescriptorSupplier {
    DialogControlServiceFileDescriptorSupplier() {}
  }

  private static final class DialogControlServiceMethodDescriptorSupplier
      extends DialogControlServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DialogControlServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DialogControlServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DialogControlServiceFileDescriptorSupplier())
              .addMethod(getProcessMethod())
              .addMethod(getSignOnMethod())
              .addMethod(getSignOffMethod())
              .addMethod(getEchoMethod())
              .build();
        }
      }
    }
    return result;
  }
}
