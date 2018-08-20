/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mktdata.MatchEventIndicator;
import mktdata.QuoteRequest39;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
class Message39Decoder 
{
    private final int template = 39;
    private final boolean option;
    private String tableName;
    private final QuoteRequest39 marketData = new QuoteRequest39();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private String MEI;
    
    public Message39Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
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
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(buffer.getInt() & 0xffffffff);
       colData.add(buffer.getLong() & 0xffffffff);
       colData.add(marketData.sbeTemplateId());
       colData.add(marketData.sbeSemanticType());
       colData.add(marketData.transactTime());
       
        try 
        {
            colData.add(new String(message, 0, marketData.getQuoteReqID(message, 0),QuoteRequest39.quoteReqIDCharacterEncoding()));
        } 
        catch (UnsupportedEncodingException ex) 
        {
            System.out.println("UnsupportedEncodingException template 39" + ex);
        }
       
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
       
       for(final QuoteRequest39.NoRelatedSym noRelatedSym : marketData.noRelatedSym())
       {
           try 
           {          
               colData.add(new String(message, 0, noRelatedSym.getSymbol(message, 0), QuoteRequest39.NoRelatedSym.symbolCharacterEncoding()));
           } 
           catch (UnsupportedEncodingException ex) 
           {
               System.out.println("UnsupportedEncodingException template 39" + ex);
           }  
           
           colData.add(noRelatedSym.securityID());           
           colData.add(noRelatedSym.orderQty());           
           colData.add(noRelatedSym.quoteType());           
           colData.add(noRelatedSym.side()); 
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.quoterequest39_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
            + "QuoteReqID_131,MatchEventIndicator_5799,Symbol_55,SecurityId_48,"
            + "OrderQty_38,QuoteType_537,Side_54) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.quoterequest39 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,"
            + "QuoteReqID_131,MatchEventIndicator_5799,Symbol_55,SecurityId_48,"
            + "OrderQty_38,QuoteType_537,Side_54) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);

       return dbo;
    }
    
}


