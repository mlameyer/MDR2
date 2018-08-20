/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import mktdata.ChannelReset4;
import mktdata.MatchEventIndicator;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;
/**
 *
 * @author Administrator
 */
class Message4Decoder 
{
    private final int template = 4;
    private final boolean option;
    private String tableName;
    private final ChannelReset4 marketData = new ChannelReset4();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private String MEI;
    
    public Message4Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
    {
       this.message = message;
       this.bufferOffset = bufferOffset;
       this.actingBlockLength = actingBlockLength;
       this.actingVersion = actingVersion;
       this.option = option;
    }

    public DatabaseObject decode() {
        final DirectBuffer directBuffer = new DirectBuffer(message);
        final ByteBuffer buffer = ByteBuffer.wrap(message);
        StringBuilder sbmd = new StringBuilder();
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        DatabaseObject dbo = new DatabaseObject();
        ArrayList colData = new ArrayList();
       
        marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
        colData.add(buffer.getInt() & 0xffffffff);
        colData.add(buffer.getLong() & 0xffffffff);
        colData.add(marketData.sbeTemplateId());
        colData.add(marketData.sbeSemanticType());
        colData.add(marketData.transactTime());
        
        final MatchEventIndicator event = marketData.matchEventIndicator();
        if(event.EndOfEvent()){MEI = "1";}else{MEI = "0";}
        sbmd.append(MEI); 
        if(event.LastQuoteMsg()){MEI = "1";}else{MEI = "0";}
        sbmd.append(MEI);
        if(event.LastImpliedMsg()){MEI = "1";}else{MEI = "0";}
        sbmd.append(MEI); 
        if(event.LastStatsMsg()){MEI = "1";}else{MEI = "0";}
        sbmd.append(MEI);
        if(event.LastTradeMsg()){MEI = "1";}else{MEI = "0";}
        sbmd.append(MEI); 
        if(event.LastVolumeMsg()){MEI = "1";}else{MEI = "0";}
        sbmd.append(MEI);
        if(event.RecoveryMsg()){MEI = "1";}else{MEI = "0";}
        sbmd.append(MEI); 
        if(event.Reserved()){MEI = "1";}else{MEI = "0";}
        colData.add(sbmd.append(MEI));

        for(final ChannelReset4.NoMDEntries noMDEntries : marketData.noMDEntries())
        {         
            colData.add(noMDEntries.mDUpdateAction());           
            colData.add(noMDEntries.getMDEntryType(message, 0, message.length));
            colData.add(noMDEntries.applID());
            
        }
        
        if(option)
        {
            tableName = "INSERT INTO "
             + "cme_market_datafeed_b.channelreset4_options "
             + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
                    + "MatchEventIndicator_5799,MDUpdateAction_279,"
                    + "MDEntryType_269,ApplID_1180) "
             + "VALUES (?,?,?,?,?,?,?,?,?)";
        }
        else
        {
            tableName = "INSERT INTO "
             + "cme_market_datafeed_b.channelreset4 "
             + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
                    + "MatchEventIndicator_5799,MDUpdateAction_279,"
                    + "MDEntryType_269,ApplID_1180) "
             + "VALUES (?,?,?,?,?,?,?,?,?)";
        }

        dbo.setTemplate(template);
        dbo.setTableName(tableName);
        dbo.setColData(colData);

        return dbo;
    }
}
