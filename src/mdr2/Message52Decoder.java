/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import mktdata.SettlPriceType;
import mktdata.SnapshotFullRefresh52;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
class Message52Decoder 
{
    private final int template = 38;
    private final boolean option;
    private String tableName;
    private final SnapshotFullRefresh52 marketData = new SnapshotFullRefresh52();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private String SPT;
    
    public Message52Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
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
       colData.add(marketData.lastMsgSeqNumProcessed());
       colData.add(marketData.totNumReports());
       colData.add(marketData.securityID());
       colData.add(marketData.rptSeq());
       colData.add(marketData.transactTime());
       colData.add(marketData.lastUpdateTime());
       colData.add(marketData.tradeDate());
       colData.add(marketData.mDSecurityTradingStatus().ordinal());
       colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
       colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
       colData.add(pf.formatPrice(marketData.maxPriceVariation().mantissa(), marketData.maxPriceVariation().exponent()));
       
       for(final SnapshotFullRefresh52.NoMDEntries noMDEntries : marketData.noMDEntries())
       {
           colData.add(pf.formatPrice(noMDEntries.mDEntryPx().mantissa(), noMDEntries.mDEntryPx().exponent()));
           colData.add(noMDEntries.mDEntrySize());                   
           colData.add(noMDEntries.numberOfOrders());           
           colData.add(noMDEntries.mDPriceLevel()); 
           colData.add(noMDEntries.tradingReferenceDate()); 
           colData.add(noMDEntries.openCloseSettlFlag().ordinal());   
                      
           final SettlPriceType event1 = noMDEntries.settlPriceType();
           if(event1.Actual()){SPT = "1";}else{SPT = "0";}
           sbmd.append(SPT); 
           if(event1.FinalDaily()){SPT = "1";}else{SPT = "0";}
           sbmd.append(SPT);
           if(event1.Intraday()){SPT = "1";}else{SPT = "0";}
           sbmd.append(SPT); 
           if(event1.NullValue()){SPT = "1";}else{SPT = "0";}
           sbmd.append(SPT);
           if(event1.ReservedBits()){SPT = "1";}else{SPT = "0";}
           sbmd.append(SPT); 
           if(event1.Rounded()){SPT = "1";}else{SPT = "0";}
           colData.add(sbmd.append(SPT));
       
           colData.add(noMDEntries.mDEntryType());
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.snapshotfullrefresh38_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,LastMsgSeqNumProcessed_369,"
            + "TotNumReports_911,SecurityId_48,Rpt_Seq_83,TransactTime_60,"
            + "LastUpdateTime_779,TradeDate_75,MDSecurityTradingStatus_1682,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MDEntryPx_270,MDEntrySize_271,NumberOfOrders_346,MDPriceLevel_1023,"
            + "TradingReferenceDate_5796,OpenCloseSettlFlag_286,SettlPriceType_731,"
            + "MDEntryType_269) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.snapshotfullrefresh38 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,LastMsgSeqNumProcessed_369,"
            + "TotNumReports_911,SecurityId_48,Rpt_Seq_83,TransactTime_60,"
            + "LastUpdateTime_779,TradeDate_75,MDSecurityTradingStatus_1682,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MDEntryPx_270,MDEntrySize_271,NumberOfOrders_346,MDPriceLevel_1023,"
            + "TradingReferenceDate_5796,OpenCloseSettlFlag_286,SettlPriceType_731,"
            + "MDEntryType_269) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);

       return dbo;
    }
    
}


