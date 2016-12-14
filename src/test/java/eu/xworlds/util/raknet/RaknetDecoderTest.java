package eu.xworlds.util.raknet;

import static com.google.common.truth.Truth.assertThat;

import eu.xworlds.util.raknet.protocol.DecodeException;
import eu.xworlds.util.raknet.protocol.OpenConnectionRequest1;
import eu.xworlds.util.raknet.protocol.RaknetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.UdpPacket;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

// NOTE(leo): only care about types
// 5 ID_OPEN_CONNECTION_REQUEST_1
// 6 ID_OPEN_CONNECTION_REPLY_1
// 7 ID_OPEN_CONNECTION_REQUEST_2
// 8 ID_OPEN_CONNECTION_REPLY_2
// 0x84 READY_PACKET
// 0x8c ADD_ENTITY_PACKET
// 0xc0 ACK

// http://wiki.vg/Pocket_Edition_Protocol_Documentation

public class RaknetDecoderTest {
    PcapHandle handle;
    RaknetDecoder decoder = new RaknetDecoder();

    @org.junit.Before
    public void setUp() throws Exception {
        handle = Pcaps.openOffline("src/test/resources/sample-gamex-single-stream.pcapng");
        handle.getNextPacket();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        handle.close();
    }

    @org.junit.Test
    public void decode() throws Exception {
        UdpPacket udp = handle.getNextPacketEx().get(UdpPacket.class);
        byte[] payload = udp.getPayload().getRawData();

        ByteBuf data1 = Unpooled.wrappedBuffer(payload);
        ByteBuf data2 = data1.copy();
        UdpPacket.UdpHeader header = udp.getHeader();
        InetSocketAddress recipient = new InetSocketAddress(header.getDstPort().valueAsInt());
        InetSocketAddress sender = new InetSocketAddress(header.getSrcPort().valueAsInt());
        DatagramPacket packet = new DatagramPacket(data1, recipient, sender);

        assertThat(recipient.getPort()).isEqualTo(7000);
        assertThat(sender.getPort()).isEqualTo(58631);

        data2 = data2.order(ByteOrder.BIG_ENDIAN);
        assertThat(data2.readByte()).isEqualTo((byte) 0x05);
        OpenConnectionRequest1 msg = OpenConnectionRequest1.decodeBody(data2);

        byte[] padding = new byte[1354];
        Arrays.fill(padding, (byte) 0);
        OpenConnectionRequest1 msg2 = OpenConnectionRequest1.create((byte) 6, padding);

        assertThat(msg).isEqualTo(msg2);
        assertThat(msg).isEqualTo(decodeSingle(packet));



//        assertThat(out).containsExactly(msg);
    }

    private RaknetMessage decodeSingle(DatagramPacket packet)
            throws DecodeException {
        ArrayList<Object> out = new ArrayList<>(1);
        decoder.decode(null, packet, out);
        return (RaknetMessage) out.get(0);
    }

}