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
import mktdata.InstAttribValue;
import mktdata.MDInstrumentDefinitionSpread56;
import mktdata.MatchEventIndicator;
import mktdata.SettlPriceType;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;
/**
 *
 * @author Administrator
 */
class Message56Decoder 
{
    private final boolean option;
    private String tableName;
    private final MDInstrumentDefinitionSpread56 marketData = new MDInstrumentDefinitionSpread56();
    private final byte[] message;
    private final int bufferOffset;
    private final int actingBlockLength;
    private final int actingVersion;
    private ByteBuffer buffer;
    private int seqnum;
    private long transact;
    private String MEI;
    private String SPT;
    private String IAV;
    
    public Message56Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
    {
       this.message = message;
       this.bufferOffset = bufferOffset;
       this.actingBlockLength = actingBlockLength;
       this.actingVersion = actingVersion;
       this.option = option;
    }
    
    public DatabaseObject decodeNoEvents() 
    {
       final int template = 29; 
       final DirectBuffer directBuffer = new DirectBuffer(message);
       buffer = ByteBuffer.wrap(message);
       StringBuilder sbmd;
       buffer.order(ByteOrder.LITTLE_ENDIAN);
       PriceFormatting pf = new PriceFormatting();
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       seqnum = buffer.getInt() & 0xffffffff;
       transact = buffer.getLong() & 0xffffffff;
       
       marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(seqnum);
       colData.add(transact);
       colData.add(marketData.sbeTemplateId());
       colData.add(marketData.sbeSemanticType());
       
       final MatchEventIndicator event = marketData.matchEventIndicator();
       sbmd = new StringBuilder();
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
       
       colData.add("REPLAY ONLY"); //.append(marketData.totNumReports());
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY"); //.append(marketData.mDSecurityTradingStatus()); instrument Replay feed only
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionSpread56.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionSpread56.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionSpread56.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionSpread56.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionSpread56.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionSpread56.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionSpread56.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecuritySubType(message, 0),MDInstrumentDefinitionSpread56.securitySubTypeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
       colData.add(marketData.userDefinedInstrument());
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.priceDisplayFormat());
       colData.add(pf.formatPrice(marketData.priceRatio().mantissa(), marketData.priceRatio().exponent()));
       colData.add(marketData.tickRule());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionSpread56.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(pf.formatPrice(marketData.tradingReferencePrice().mantissa(), marketData.tradingReferencePrice().exponent()));

       final SettlPriceType event1 = marketData.settlPriceType();
       StringBuilder md = new StringBuilder();
       if(event1.Actual()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.FinalDaily()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.Intraday()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.NullValue()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.ReservedBits()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.Rounded()){SPT = "1";}else{SPT = "0";}
       colData.add(md.append(SPT));

       colData.add(marketData.openInterestQty());
       colData.add(marketData.clearedVolume());
       colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
       colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
       colData.add(pf.formatPrice(marketData.maxPriceVariation().mantissa(), marketData.maxPriceVariation().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       
       for(final MDInstrumentDefinitionSpread56.NoEvents noEvents : marketData.noEvents())
       {
           colData.add(noEvents.eventType());          
           colData.add(noEvents.eventTime()); 
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,EventType_865,EventTime_1145) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,EventType_865,EventTime_1145) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
    public DatabaseObject decodeNoMDFeedTypes() 
    {
       final int template = 291; 
       final DirectBuffer directBuffer = new DirectBuffer(message);
       StringBuilder sbmd;
       PriceFormatting pf = new PriceFormatting();
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(seqnum);
       colData.add(transact);
       colData.add(marketData.sbeTemplateId());
       colData.add(marketData.sbeSemanticType());
       
       final MatchEventIndicator event = marketData.matchEventIndicator();
       sbmd = new StringBuilder();
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
       
       colData.add("REPLAY ONLY"); //.append(marketData.totNumReports());
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY"); //.append(marketData.mDSecurityTradingStatus()); instrument Replay feed only
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionSpread56.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionSpread56.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionSpread56.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionSpread56.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionSpread56.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionSpread56.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionSpread56.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecuritySubType(message, 0),MDInstrumentDefinitionSpread56.securitySubTypeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
       colData.add(marketData.userDefinedInstrument());
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.priceDisplayFormat());
       colData.add(pf.formatPrice(marketData.priceRatio().mantissa(), marketData.priceRatio().exponent()));
       colData.add(marketData.tickRule());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionSpread56.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(pf.formatPrice(marketData.tradingReferencePrice().mantissa(), marketData.tradingReferencePrice().exponent()));

       final SettlPriceType event1 = marketData.settlPriceType();
       StringBuilder md = new StringBuilder();
       if(event1.Actual()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.FinalDaily()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.Intraday()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.NullValue()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.ReservedBits()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.Rounded()){SPT = "1";}else{SPT = "0";}
       colData.add(md.append(SPT));

       colData.add(marketData.openInterestQty());
       colData.add(marketData.clearedVolume());
       colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
       colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
       colData.add(pf.formatPrice(marketData.maxPriceVariation().mantissa(), marketData.maxPriceVariation().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       
       for(final MDInstrumentDefinitionSpread56.NoEvents noEvents : marketData.noEvents())
       {
           noEvents.eventType();          
           noEvents.eventTime();
       }
       
       for(final MDInstrumentDefinitionSpread56.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
       {
           try 
           {          
               colData.add(new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionSpread56.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim()));
           } catch (UnsupportedEncodingException ex) 
           {
                System.out.println("UnsupportedEncodingException template 56" + ex);
           }
           colData.add(noMDFeedTypes.marketDepth()); 
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29NoMDFeedTypes_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,MDFeedType_1022,MarketDepth_264) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29NoMDFeedTypes "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,MDFeedType_1022,MarketDepth_264) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
    public DatabaseObject decodeNoInstAttrib() 
    {
       final int template = 291; 
       final DirectBuffer directBuffer = new DirectBuffer(message);
       StringBuilder sbmd;
       PriceFormatting pf = new PriceFormatting();
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(seqnum);
       colData.add(transact);
       colData.add(marketData.sbeTemplateId());
       colData.add(marketData.sbeSemanticType());
       
       final MatchEventIndicator event = marketData.matchEventIndicator();
       sbmd = new StringBuilder();
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
       
       colData.add("REPLAY ONLY"); //.append(marketData.totNumReports());
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY"); //.append(marketData.mDSecurityTradingStatus()); instrument Replay feed only
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionSpread56.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionSpread56.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionSpread56.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionSpread56.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionSpread56.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionSpread56.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
      
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionSpread56.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecuritySubType(message, 0),MDInstrumentDefinitionSpread56.securitySubTypeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
       colData.add(marketData.userDefinedInstrument());
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.priceDisplayFormat());
       colData.add(pf.formatPrice(marketData.priceRatio().mantissa(), marketData.priceRatio().exponent()));
       colData.add(marketData.tickRule());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionSpread56.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(pf.formatPrice(marketData.tradingReferencePrice().mantissa(), marketData.tradingReferencePrice().exponent()));

       final SettlPriceType event1 = marketData.settlPriceType();
       StringBuilder md = new StringBuilder();
       if(event1.Actual()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.FinalDaily()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.Intraday()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.NullValue()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.ReservedBits()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.Rounded()){SPT = "1";}else{SPT = "0";}
       colData.add(md.append(SPT));

       colData.add(marketData.openInterestQty());
       colData.add(marketData.clearedVolume());
       colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
       colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
       colData.add(pf.formatPrice(marketData.maxPriceVariation().mantissa(), marketData.maxPriceVariation().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       
       for(final MDInstrumentDefinitionSpread56.NoEvents noEvents : marketData.noEvents())
       {
           noEvents.eventType();          
           noEvents.eventTime(); 
       }
       
       for(final MDInstrumentDefinitionSpread56.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
       {
           try 
           {          
               String na = new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionSpread56.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim());
           } catch (UnsupportedEncodingException ex) 
           {
                System.out.println("UnsupportedEncodingException template 56" + ex);
           }
           noMDFeedTypes.marketDepth();
       }
       
       for(final MDInstrumentDefinitionSpread56.NoInstAttrib noInstAttrib : marketData.noInstAttrib())
       {
           colData.add(noInstAttrib.instAttribType());          
      
            final InstAttribValue event2 = noInstAttrib.instAttribValue();
            StringBuilder d = new StringBuilder();
            if(event2.BlockTradeEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.DailyProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.DecayingProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.EBFEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.EFPEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.EFREligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.EFSEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ElectronicMatchEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.GTOrdersEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ImpliedMatchingEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.IsFractional()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.NegativePriceOutrightEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.NegativeStrikeEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.OTCEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.OrderCrossEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.RFQCrossEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.VariableProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.VolatilityQuotedOption()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ZeroPriceOutrightEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.iLinkIndicativeMassQuotingEligible()){IAV = "1";}else{IAV = "0";}
            colData.add(d.append(IAV));
            
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29NoInstAttrib_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,InstAttribType_871,InstAttribValue_872) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29NoInstAttrib "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,InstAttribType_871,InstAttribValue_872) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
    public DatabaseObject decodeNoLotTypeRules() 
    {
       final int template = 291; 
       final DirectBuffer directBuffer = new DirectBuffer(message);
       StringBuilder sbmd;
       PriceFormatting pf = new PriceFormatting();
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(seqnum);
       colData.add(transact);
       colData.add(marketData.sbeTemplateId());
       colData.add(marketData.sbeSemanticType());
       
       final MatchEventIndicator event = marketData.matchEventIndicator();
       sbmd = new StringBuilder();
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
       
       colData.add("REPLAY ONLY"); //.append(marketData.totNumReports());
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY"); //.append(marketData.mDSecurityTradingStatus()); instrument Replay feed only
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionSpread56.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionSpread56.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionSpread56.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionSpread56.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionSpread56.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionSpread56.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionSpread56.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecuritySubType(message, 0),MDInstrumentDefinitionSpread56.securitySubTypeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
       colData.add(marketData.userDefinedInstrument());
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.priceDisplayFormat());
       colData.add(pf.formatPrice(marketData.priceRatio().mantissa(), marketData.priceRatio().exponent()));
       colData.add(marketData.tickRule());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionSpread56.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(pf.formatPrice(marketData.tradingReferencePrice().mantissa(), marketData.tradingReferencePrice().exponent()));

       final SettlPriceType event1 = marketData.settlPriceType();
       StringBuilder md = new StringBuilder();
       if(event1.Actual()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.FinalDaily()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.Intraday()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.NullValue()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.ReservedBits()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.Rounded()){SPT = "1";}else{SPT = "0";}
       colData.add(md.append(SPT));

       colData.add(marketData.openInterestQty());
       colData.add(marketData.clearedVolume());
       colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
       colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
       colData.add(pf.formatPrice(marketData.maxPriceVariation().mantissa(), marketData.maxPriceVariation().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       
       for(final MDInstrumentDefinitionSpread56.NoEvents noEvents : marketData.noEvents())
       {
           noEvents.eventType();          
           noEvents.eventTime();
       }
       
       for(final MDInstrumentDefinitionSpread56.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
       {
           try 
           {          
               String na = new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionSpread56.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim());
           } catch (UnsupportedEncodingException ex) 
           {
                System.out.println("UnsupportedEncodingException template 56" + ex);
           }
           noMDFeedTypes.marketDepth();
       }
       
       for(final MDInstrumentDefinitionSpread56.NoInstAttrib noInstAttrib : marketData.noInstAttrib())
       {
            noInstAttrib.instAttribType();          
      
            final InstAttribValue event2 = noInstAttrib.instAttribValue();
            StringBuilder d = new StringBuilder();
            if(event2.BlockTradeEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.DailyProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.DecayingProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.EBFEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.EFPEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.EFREligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.EFSEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ElectronicMatchEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.GTOrdersEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ImpliedMatchingEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.IsFractional()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.NegativePriceOutrightEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.NegativeStrikeEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.OTCEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.OrderCrossEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.RFQCrossEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.VariableProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.VolatilityQuotedOption()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ZeroPriceOutrightEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.iLinkIndicativeMassQuotingEligible()){IAV = "1";}else{IAV = "0";}
       }
       
       for(final MDInstrumentDefinitionSpread56.NoLotTypeRules noLotTypeRules : marketData.noLotTypeRules())
       {
           colData.add(noLotTypeRules.lotType());          
           colData.add(pf.formatPrice(noLotTypeRules.minLotSize().mantissa(), noLotTypeRules.minLotSize().exponent()));
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29NoLotTypeRules_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,LotType_1093,MinLotSize_1231) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29NoLotTypeRules "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,LotType_1093,MinLotSize_1231) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
    public DatabaseObject decodeNoLegs() 
    {
       final int template = 292; 
       final DirectBuffer directBuffer = new DirectBuffer(message);
       StringBuilder sbmd;
       PriceFormatting pf = new PriceFormatting();
       DatabaseObject dbo = new DatabaseObject();
       ArrayList colData = new ArrayList();
       
       marketData.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);
       colData.add(seqnum);
       colData.add(transact);
       colData.add(marketData.sbeTemplateId());
       colData.add(marketData.sbeSemanticType());
       
       final MatchEventIndicator event = marketData.matchEventIndicator();
       sbmd = new StringBuilder();
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
       
       colData.add("REPLAY ONLY"); //.append(marketData.totNumReports());
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY"); //.append(marketData.mDSecurityTradingStatus()); instrument Replay feed only
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionSpread56.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionSpread56.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionSpread56.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionSpread56.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionSpread56.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionSpread56.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionSpread56.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecuritySubType(message, 0),MDInstrumentDefinitionSpread56.securitySubTypeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
       colData.add(marketData.userDefinedInstrument());
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.priceDisplayFormat());
       colData.add(pf.formatPrice(marketData.priceRatio().mantissa(), marketData.priceRatio().exponent()));
       colData.add(marketData.tickRule());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionSpread56.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
        System.out.println("UnsupportedEncodingException template 56" + ex);
      }
  
       colData.add(pf.formatPrice(marketData.tradingReferencePrice().mantissa(), marketData.tradingReferencePrice().exponent()));

       final SettlPriceType event1 = marketData.settlPriceType();
       StringBuilder md = new StringBuilder();
       if(event1.Actual()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.FinalDaily()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.Intraday()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.NullValue()){SPT = "1";}else{SPT = "0";}
       md.append(SPT);
       if(event1.ReservedBits()){SPT = "1";}else{SPT = "0";}
       md.append(SPT); 
       if(event1.Rounded()){SPT = "1";}else{SPT = "0";}
       colData.add(md.append(SPT));

       colData.add(marketData.openInterestQty());
       colData.add(marketData.clearedVolume());
       colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
       colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
       colData.add(pf.formatPrice(marketData.maxPriceVariation().mantissa(), marketData.maxPriceVariation().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       
       for(final MDInstrumentDefinitionSpread56.NoEvents noEvents : marketData.noEvents())
       {
           noEvents.eventType();          
           noEvents.eventTime();
       }
       
       for(final MDInstrumentDefinitionSpread56.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
       {
           try 
           {          
               String na = new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionSpread56.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim());
           } catch (UnsupportedEncodingException ex) 
           {
                System.out.println("UnsupportedEncodingException template 56" + ex);
           }
           noMDFeedTypes.marketDepth();
       }
       
       for(final MDInstrumentDefinitionSpread56.NoInstAttrib noInstAttrib : marketData.noInstAttrib())
       {
            noInstAttrib.instAttribType();          
      
            final InstAttribValue event2 = noInstAttrib.instAttribValue();
            StringBuilder d = new StringBuilder();
            if(event2.BlockTradeEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.DailyProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.DecayingProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.EBFEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.EFPEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV); 
            if(event2.EFREligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.EFSEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ElectronicMatchEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.GTOrdersEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ImpliedMatchingEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.IsFractional()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.NegativePriceOutrightEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.NegativeStrikeEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.OTCEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.OrderCrossEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.RFQCrossEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.VariableProductEligibility()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.VolatilityQuotedOption()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.ZeroPriceOutrightEligible()){IAV = "1";}else{IAV = "0";}
            d.append(IAV);
            if(event2.iLinkIndicativeMassQuotingEligible()){IAV = "1";}else{IAV = "0";}
       }
       
       for(final MDInstrumentDefinitionSpread56.NoLotTypeRules noLotTypeRules : marketData.noLotTypeRules())
       {
           noLotTypeRules.lotType();          
           pf.formatPrice(noLotTypeRules.minLotSize().mantissa(), noLotTypeRules.minLotSize().exponent());
       }
       
       for(final MDInstrumentDefinitionSpread56.NoLegs noLegs : marketData.noLegs())
       {
           colData.add(noLegs.legSecurityID());          
           colData.add(new String(message, 0, noLegs.getLegSecurityIDSource(message, 0, message.length)));  
           colData.add(noLegs.legSide()); 
           colData.add(noLegs.legRatioQty()); 
           colData.add(pf.formatPrice(noLegs.legPrice().mantissa(), noLegs.legPrice().exponent())); 
           colData.add(pf.formatPrice(noLegs.legOptionDelta().mantissa(), noLegs.legOptionDelta().exponent())); 
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29NoLegs_options "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,LegSecurityID_602,"
            + "LegSecurityIDSource_603,LegSide_624,LegRatioQty_623,"
            + "LegPrice_566,LegOptionDelta_1017)"
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed.mdinstrumentdefinitionspread29NoLegs "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,"
            + "MatchEventIndicator_5799,TotNumReports_911,SecurityUpdateAction_980,"
            + "LastUpdateTime_779,MDSecurityTradingStatus_1682,ApplID_1180,"
            + "MarketSegmentID_1300,UnderlyingProduct_462,SecurityExchange_207,"
            + "SecurityGroup_1151,Asset_6937,Symbol_55,SecurityId_48,"
            + "SecurityIDSource_22,SecurityType_167,CFICode_461,"
            + "MaturityMonthYear_200,Currency_15,SecuritySubType_762,"
            + "UserDefinedInstrument_9779,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "PriceDisplayFormat_9800,PriceRatio_5770,TickRule_6350,"
            + "UnitOfMeasure_996,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "MainFraction_37702,SubFraction_37703,LegSecurityID_602,"
            + "LegSecurityIDSource_603,LegSide_624,LegRatioQty_623,"
            + "LegPrice_566,LegOptionDelta_1017)"
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
}
