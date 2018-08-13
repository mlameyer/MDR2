/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import mktdata.MDIncrementalRefreshLimitsBanding50;
import mktdata.MatchEventIndicator;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
class Message50Decoder 
{
    private final int template = 34;
    private final boolean option;
    private String tableName;
    private final MDIncrementalRefreshLimitsBanding50 marketData = new MDIncrementalRefreshLimitsBanding50();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private String MEI;
    
    public Message50Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
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
       
       for(final MDIncrementalRefreshLimitsBanding50.NoMDEntries noMDEntries : marketData.noMDEntries())
       {
           colData.add(pf.formatPrice(noMDEntries.highLimitPrice().mantissa(), noMDEntries.highLimitPrice().exponent()));
           colData.add(pf.formatPrice(noMDEntries.lowLimitPrice().mantissa(), noMDEntries.lowLimitPrice().exponent())); 
           colData.add(pf.formatPrice(noMDEntries.maxPriceVariation().mantissa(), noMDEntries.maxPriceVariation().exponent()));
           colData.add(noMDEntries.securityID());           
           colData.add(noMDEntries.rptSeq());                             
           colData.add(noMDEntries.mDUpdateAction());           
           colData.add(noMDEntries.getMDEntryType(message, 0, message.length));
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdincrementalrefreshlimitsbanding34_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,MatchEventIndicator_5799,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "SecurityId_48,Rpt_Seq_83,MDUpdateAction_279,MDEntryType_269) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdincrementalrefreshlimitsbanding34 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,MatchEventIndicator_5799,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "SecurityId_48,Rpt_Seq_83,MDUpdateAction_279,MDEntryType_269) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);

       return dbo;
    }
    
}
