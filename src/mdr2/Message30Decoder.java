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
import mktdata.MatchEventIndicator;
import mktdata.SecurityStatus30;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
class Message30Decoder 
{
    private final int template = 30;
    private final boolean option;
    private String tableName;
    private final SecurityStatus30 marketData = new SecurityStatus30();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private String MEI;

    public Message30Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
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
            colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0), SecurityStatus30.securityGroupCharacterEncoding()));
            colData.add(new String(message, 0, marketData.getAsset(message, 0), SecurityStatus30.assetCharacterEncoding()));
        } 
        catch (UnsupportedEncodingException ex) 
        {
            System.out.println("UnsupportedEncodingException template 30" + ex);
        }
        
        colData.add(marketData.securityID());
        colData.add(marketData.tradeDate());
        
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
        
        colData.add(marketData.securityTradingStatus().ordinal());
        colData.add(marketData.haltReason().ordinal());
        colData.add(marketData.securityTradingEvent().ordinal());
       
        if(option)
        {
            tableName = "INSERT INTO "
             + "cme_market_datafeed_b.securitystatus30_options "
             + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,SecurityGroup_1151,"
             + "Asset_6937,SecurityId_48,TradeDate_75,MatchEventIndicator_5799,"
             + "SecurityTradingStatus_326,HaltReason_327,SecurityTradingEvent_1174) "
             + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        else
        {
            tableName = "INSERT INTO "
             + "cme_market_datafeed_b.securitystatus30 "
             + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,TransactTime_60,SecurityGroup_1151,"
             + "Asset_6937,SecurityId_48,TradeDate_75,MatchEventIndicator_5799,"
             + "SecurityTradingStatus_326,HaltReason_327,SecurityTradingEvent_1174) "
             + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);

       return dbo;
    }
    
}
