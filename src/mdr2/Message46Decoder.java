/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mdr2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import mktdata.MDIncrementalRefreshBook46;
import mktdata.MatchEventIndicator;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
public class Message46Decoder 
{
    private final int template = 32;
    private final boolean option;
    private String tableName;
    private final MDIncrementalRefreshBook46 marketData = new MDIncrementalRefreshBook46();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private String MEI;
    
    public Message46Decoder(final byte[] message, final int bufferOffset, final int actingBlockLength, final int actingVersion, boolean option) 
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
       StringBuilder sbmd = new StringBuilder();
       buffer.order(ByteOrder.LITTLE_ENDIAN);
       PriceFormatting pf = new PriceFormatting();
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

       for(final MDIncrementalRefreshBook46.NoMDEntries noMDEntries : marketData.noMDEntries())
       {
           colData.add(pf.formatPrice(noMDEntries.mDEntryPx().mantissa(), noMDEntries.mDEntryPx().exponent()));
           colData.add(noMDEntries.mDEntrySize());
           colData.add(noMDEntries.securityID());
           colData.add(noMDEntries.rptSeq());
           colData.add(noMDEntries.numberOfOrders());
           colData.add(noMDEntries.mDPriceLevel());
           colData.add(noMDEntries.mDUpdateAction().ordinal());
           colData.add(noMDEntries.mDEntryType().ordinal());
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdincrementalrefreshbook32_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
            + "MatchEventIndicator_5799,MDEntryPx_270,MDEntrySize_271,"
            + "SecurityId_48,Rpt_Seq_83,NumberOfOrders_346,MDPriceLevel_1023,"
            + "MDUpdateAction_279,MDEntryType_269) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdincrementalrefreshbook32 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
            + "MatchEventIndicator_5799,MDEntryPx_270,MDEntrySize_271,"
            + "SecurityId_48,Rpt_Seq_83,NumberOfOrders_346,MDPriceLevel_1023,"
            + "MDUpdateAction_279,MDEntryType_269) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);

       return dbo;
    }
}
