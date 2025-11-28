package com.bbva.orchlib.featuretoggle.channelgrpc;

import io.grpc.ManagedChannel;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicReference;

@Getter
public class ChannelGrpc {
    @Setter
    private String name;
    @Setter
    private int port;
    private final AtomicReference<ManagedChannel> managedChannel = new AtomicReference<>();

    public ManagedChannel getManagedChannel() {
        return managedChannel.get();
    }

    public void setManagedChannel(ManagedChannel managedChannel) {
        ManagedChannel currentChannel = this.managedChannel.get();
        if (!this.managedChannel.compareAndSet(currentChannel, managedChannel)) {
            managedChannel.shutdown();
        }
    }
}
