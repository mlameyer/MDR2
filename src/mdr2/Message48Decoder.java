/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mdr2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import mktdata.MDIncrementalRefreshTradeSummary48;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;
import mktdata.MatchEventIndicator;

/**
 *
 * @author Administrator
 */
public class Message48Decoder 
{
    private final boolean option;
    private String tableName;
    private final MDIncrementalRefreshTradeSummary48 marketData = new MDIncrementalRefreshTradeSummary48();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private ByteBuffer buffers;
    private int seqnum;
    private long transact;
    private String MEI;
    
    public Message48Decoder(final byte[] message, final int bufferOffset, final int actingBlockLength, final int actingVersion, boolean option) 
    {
       this.message = message;
       this.bufferOffset = bufferOffset;
       this.actingBlockLength = actingBlockLength;
       this.actingVersion = actingVersion;
       this.option = option;
    }
    
    public DatabaseObject decodeNoMDEntries()
    {
       final int template = 42;
       final DirectBuffer directBuffer = new DirectBuffer(message);
       buffers = ByteBuffer.wrap(message);
       StringBuilder sbmd = new StringBuilder();
       buffers.order(ByteOrder.LITTLE_ENDIAN);
       PriceFormatting pf = new PriceFormatting();
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       seqnum = buffers.getInt() & 0xffffffff;
       transact = buffers.getLong() & 0xffffffff;
       
       marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(seqnum);
       colData.add(transact);
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
       
       for(final MDIncrementalRefreshTradeSummary48.NoMDEntries noMDEntries : marketData.noMDEntries())
       {
           colData.add(pf.formatPrice(noMDEntries.mDEntryPx().mantissa(), noMDEntries.mDEntryPx().exponent()));
           colData.add(noMDEntries.mDEntrySize());          
           colData.add(noMDEntries.securityID());           
           colData.add(noMDEntries.rptSeq());           
           colData.add(noMDEntries.numberOfOrders());           
           colData.add(noMDEntries.aggressorSide().ordinal());
           colData.add(noMDEntries.mDUpdateAction().ordinal());  
           colData.add(noMDEntries.getMDEntryType(message, 0, message.length));
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdincrementalrefreshtradesummary42_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
            + "MatchEventIndicator_5799,MDEntryPx_270,MDEntrySize_271,"
            + "SecurityId_48,Rpt_Seq_83,NumberOfOrders_346,"
            + "AggressorSide_5797,MDUpdateAction_279,MDEntryType_269) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdincrementalrefreshtradesummary42 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
            + "MatchEventIndicator_5799,MDEntryPx_270,MDEntrySize_271,"
            + "SecurityId_48,Rpt_Seq_83,NumberOfOrders_346,"
            + "AggressorSide_5797,MDUpdateAction_279,MDEntryType_269) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
    public DatabaseObject decodeNoOrderIDEntries()
    {
       final int template = 421; 
       final DirectBuffer directBuffer = new DirectBuffer(message);
       StringBuilder sbmd = new StringBuilder();
       PriceFormatting pf = new PriceFormatting();
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(seqnum);
       colData.add(transact);
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
       
       for(final MDIncrementalRefreshTradeSummary48.NoMDEntries noMDEntries : marketData.noMDEntries())
       {
           pf.formatPrice(noMDEntries.mDEntryPx().mantissa(), noMDEntries.mDEntryPx().exponent());
           noMDEntries.mDEntrySize();          
           noMDEntries.securityID();           
           noMDEntries.rptSeq();           
           noMDEntries.numberOfOrders();           
           noMDEntries.aggressorSide().ordinal();
           noMDEntries.mDUpdateAction().ordinal();  
           noMDEntries.getMDEntryType(message, 0, message.length);
       }
       
       for(final MDIncrementalRefreshTradeSummary48.NoOrderIDEntries NoOrderIDEntries : marketData.noOrderIDEntries())
       {
           colData.add(NoOrderIDEntries.orderID());
           colData.add(NoOrderIDEntries.lastQty()); 
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdincrementalrefreshtradesummary42NoOrderIDEntries_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
            + "MatchEventIndicator_5799,OrderID_37,LastQty_32) "
            + "VALUES (?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdincrementalrefreshtradesummary42NoOrderIDEntries "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
            + "MatchEventIndicator_5799,OrderID_37,LastQty_32) "
            + "VALUES (?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
}
