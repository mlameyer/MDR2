/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mdr2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import mktdata.AdminHeartbeat12;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
public class Message12Decoder 
{
    private final int template = 12;
    private final boolean option;
    private String tableName;
    private final AdminHeartbeat12 heartBeat = new AdminHeartbeat12();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    
    public Message12Decoder(final byte[] message, final int bufferOffset, final int actingBlockLength, final int actingVersion, boolean option)
    {
        this.message = message;
        this.bufferOffset = bufferOffset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        this.option = option;
    }
    
    public DatabaseObject decode()
    {
       final DirectBuffer directBuffer = new DirectBuffer(message);
       final ByteBuffer buffer = ByteBuffer.wrap(message);
       buffer.order(ByteOrder.LITTLE_ENDIAN);
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       heartBeat.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(buffer.getInt() & 0xffffffff);
       colData.add(buffer.getLong() & 0xffffffff);
       colData.add(heartBeat.sbeTemplateId());
       colData.add(heartBeat.sbeSemanticType());
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.adminheartbeat12_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35) "
            + "VALUES (?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.adminheartbeat12 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35) "
            + "VALUES (?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
}
