package com.bbva.orchlib.featuretoggle.connectionproperties;

import com.bbva.orchlib.featuretoggle.channelgrpc.ChannelGrpc;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListProxies {
    private List<ChannelGrpc> host;
    private List<ChannelGrpc> processor;
}
