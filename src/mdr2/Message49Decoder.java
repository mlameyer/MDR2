/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import mktdata.MDIncrementalRefreshDailyStatistics49;
import mktdata.MatchEventIndicator;
import mktdata.SettlPriceType;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
class Message49Decoder 
{
    private final int template = 33;
    private final boolean option;
    private String tableName;
    private final MDIncrementalRefreshDailyStatistics49 marketData = new MDIncrementalRefreshDailyStatistics49();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private String MEI, SPT;
    
    public Message49Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
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
       
       for(final MDIncrementalRefreshDailyStatistics49.NoMDEntries noMDEntries : marketData.noMDEntries())
       {
            colData.add(pf.formatPrice(noMDEntries.mDEntryPx().mantissa(), noMDEntries.mDEntryPx().exponent()));
            colData.add(noMDEntries.mDEntrySize());          
            colData.add(noMDEntries.securityID());           
            colData.add(noMDEntries.rptSeq());           
            colData.add(noMDEntries.tradingReferenceDate());

            StringBuilder sb = new StringBuilder();
            final SettlPriceType event1 = noMDEntries.settlPriceType();
            if(event1.Actual()){SPT = "1";}else{SPT = "0";}
            sb.append(SPT); 
            if(event1.FinalDaily()){SPT = "1";}else{SPT = "0";}
            sb.append(SPT);
            if(event1.Intraday()){SPT = "1";}else{SPT = "0";}
            sb.append(SPT); 
            if(event1.NullValue()){SPT = "1";}else{SPT = "0";}
            sb.append(SPT);
            if(event1.ReservedBits()){SPT = "1";}else{SPT = "0";}
            sb.append(SPT); 
            if(event1.Rounded()){SPT = "1";}else{SPT = "0";}
            colData.add(sb.append(SPT));
            
            colData.add(noMDEntries.mDUpdateAction().ordinal());           
            colData.add(noMDEntries.mDEntryType().ordinal());
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdincrementalrefreshdailystatistics33_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,MatchEventIndicator_5799,"
            + "MDEntryPx_270,MDEntrySize_271,SecurityId_48,Rpt_Seq_83,"
            + "TradingReferenceDate_5796,SettlPriceType_731,MDUpdateAction_279,"
            + "MDEntryType_269) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdincrementalrefreshdailystatistics33 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,MatchEventIndicator_5799,"
            + "MDEntryPx_270,MDEntrySize_271,SecurityId_48,Rpt_Seq_83,"
            + "TradingReferenceDate_5796,SettlPriceType_731,MDUpdateAction_279,"
            + "MDEntryType_269) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);

       return dbo;
    }
    
}
