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
import mktdata.MDInstrumentDefinitionFuture27;
import mktdata.MatchEventIndicator;
import mktdata.SettlPriceType;
import mktdata.InstAttribValue;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
class Message27Decoder 
{
    private final boolean option;
    private String tableName;
    private final MDInstrumentDefinitionFuture27 marketData = new MDInstrumentDefinitionFuture27();
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

    public Message27Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
    {
       this.message = message;
       this.bufferOffset = bufferOffset;
       this.actingBlockLength = actingBlockLength;
       this.actingVersion = actingVersion;
       this.option = option;
    }

    public DatabaseObject decodeNoEvents() 
    {
       final int template = 27;
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
       
       colData.add("REPLAY ONLY"); //Used on replay feed only marketData.totNumReports()
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY");//.append(marketData.mDSecurityTradingStatus());
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionFuture27.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionFuture27.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionFuture27.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionFuture27.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionFuture27.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionFuture27.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       StringBuilder sb = new StringBuilder();
       colData.add(sb.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionFuture27.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0),MDInstrumentDefinitionFuture27.settlCurrencyCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       colData.add(marketData.priceDisplayFormat());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionFuture27.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
       colData.add(pf.formatPrice(marketData.unitOfMeasureQty().mantissa(), marketData.unitOfMeasureQty().exponent()));
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
       colData.add(marketData.decayQuantity());
       colData.add(marketData.decayStartDate());
       colData.add(marketData.originalContractSize());
       colData.add(marketData.contractMultiplier());
       colData.add(marketData.contractMultiplierUnit());
       colData.add(marketData.flowScheduleType());
       colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
       colData.add(marketData.userDefinedInstrument());
       
       for(final MDInstrumentDefinitionFuture27.NoEvents noEvents : marketData.noEvents())
       {
           colData.add(noEvents.eventType());          
           colData.add(noEvents.eventTime()); 
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionfuture27 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,MaturityMonthYear_200,Currency_15,"
            + "SettlCurrency_120,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"//28
            + "MainFraction_37702,SubFraction_37703,PriceDisplayFormat_9800,"
            + "UnitOfMeasure_996,UnitOfMeasureQty_1147,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "DecayQuanity_5818,DecayStartDate_5819,OriginalContractSize_5849,"
            + "ContractMultiplier_231,ContractMultiplierUnit_1435,"
            + "FlowScheduleType_1439,MinPriceIncrementAmount_1146,"
            + "UserDefinedInstrument_9779,EventType_865,EventTime_1145) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionfuture27 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,MaturityMonthYear_200,Currency_15,"
            + "SettlCurrency_120,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "MainFraction_37702,SubFraction_37703,PriceDisplayFormat_9800,"
            + "UnitOfMeasure_996,UnitOfMeasureQty_1147,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "DecayQuanity_5818,DecayStartDate_5819,OriginalContractSize_5849,"
            + "ContractMultiplier_231,ContractMultiplierUnit_1435,"
            + "FlowScheduleType_1439,MinPriceIncrementAmount_1146,"
            + "UserDefinedInstrument_9779,EventType_865,EventTime_1145) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }

       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
    public DatabaseObject decodeNoMDFeedTypes() 
    {
       final int template = 271;
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
       
       colData.add("REPLAY ONLY"); //Used on replay feed only marketData.totNumReports()
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY");//.append(marketData.mDSecurityTradingStatus());
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionFuture27.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionFuture27.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionFuture27.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionFuture27.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionFuture27.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionFuture27.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       StringBuilder sb = new StringBuilder();
       colData.add(sb.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionFuture27.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0),MDInstrumentDefinitionFuture27.settlCurrencyCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       colData.add(marketData.priceDisplayFormat());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionFuture27.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
       colData.add(pf.formatPrice(marketData.unitOfMeasureQty().mantissa(), marketData.unitOfMeasureQty().exponent()));
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
       colData.add(marketData.decayQuantity());
       colData.add(marketData.decayStartDate());
       colData.add(marketData.originalContractSize());
       colData.add(marketData.contractMultiplier());
       colData.add(marketData.contractMultiplierUnit());
       colData.add(marketData.flowScheduleType());
       colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
       colData.add(marketData.userDefinedInstrument());
       
       for(final MDInstrumentDefinitionFuture27.NoEvents noEvents : marketData.noEvents())
       {
           noEvents.eventType();          
           noEvents.eventTime();
       }
       
       for(final MDInstrumentDefinitionFuture27.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
       {
           try 
           {          
               colData.add(new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionFuture27.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim()));
           } 
           catch (UnsupportedEncodingException ex) 
           {
               System.out.println("UnsupportedEncodingException template 27" + ex);
           }
           colData.add(noMDFeedTypes.marketDepth()); 
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionfuture27NoMDFeedTypes "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,MaturityMonthYear_200,Currency_15,"
            + "SettlCurrency_120,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "MainFraction_37702,SubFraction_37703,PriceDisplayFormat_9800,"
            + "UnitOfMeasure_996,UnitOfMeasureQty_1147,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "DecayQuanity_5818,DecayStartDate_5819,OriginalContractSize_5849,"
            + "ContractMultiplier_231,ContractMultiplierUnit_1435,"
            + "FlowScheduleType_1439,MinPriceIncrementAmount_1146,"
            + "UserDefinedInstrument_9779,MDFeedType_1022,MarketDepth_264) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionfuture27NoMDFeedTypes "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,MaturityMonthYear_200,Currency_15,"
            + "SettlCurrency_120,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "MainFraction_37702,SubFraction_37703,PriceDisplayFormat_9800,"
            + "UnitOfMeasure_996,UnitOfMeasureQty_1147,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "DecayQuanity_5818,DecayStartDate_5819,OriginalContractSize_5849,"
            + "ContractMultiplier_231,ContractMultiplierUnit_1435,"
            + "FlowScheduleType_1439,MinPriceIncrementAmount_1146,"
            + "UserDefinedInstrument_9779,MDFeedType_1022,MarketDepth_264) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
    public DatabaseObject decodeNoInstAttrib() 
    {
       final int template = 271; 
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
       
       colData.add("REPLAY ONLY"); //Used on replay feed only marketData.totNumReports()
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY");//.append(marketData.mDSecurityTradingStatus());
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionFuture27.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionFuture27.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionFuture27.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionFuture27.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionFuture27.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionFuture27.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       StringBuilder s = new StringBuilder();
       colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionFuture27.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0),MDInstrumentDefinitionFuture27.settlCurrencyCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       colData.add(marketData.priceDisplayFormat());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionFuture27.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
       colData.add(pf.formatPrice(marketData.unitOfMeasureQty().mantissa(), marketData.unitOfMeasureQty().exponent()));
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
       colData.add(marketData.decayQuantity());
       colData.add(marketData.decayStartDate());
       colData.add(marketData.originalContractSize());
       colData.add(marketData.contractMultiplier());
       colData.add(marketData.contractMultiplierUnit());
       colData.add(marketData.flowScheduleType());
       colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
       colData.add(marketData.userDefinedInstrument());
       
       for(final MDInstrumentDefinitionFuture27.NoEvents noEvents : marketData.noEvents())
       {
           noEvents.eventType();          
           noEvents.eventTime();
       }
       
       for(final MDInstrumentDefinitionFuture27.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
       {
           try 
           {          
               String na = new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionFuture27.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim());
           } 
           catch (UnsupportedEncodingException ex) 
           {
               System.out.println("UnsupportedEncodingException template 27" + ex);
           }
           noMDFeedTypes.marketDepth();
       }
       
       for(final MDInstrumentDefinitionFuture27.NoInstAttrib noInstAttrib : marketData.noInstAttrib())
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
            + "cme_market_datafeed_b.mdinstrumentdefinitionfuture27NoInstAttrib "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,MaturityMonthYear_200,Currency_15,"
            + "SettlCurrency_120,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "MainFraction_37702,SubFraction_37703,PriceDisplayFormat_9800,"
            + "UnitOfMeasure_996,UnitOfMeasureQty_1147,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "DecayQuanity_5818,DecayStartDate_5819,OriginalContractSize_5849,"
            + "ContractMultiplier_231,ContractMultiplierUnit_1435,"
            + "FlowScheduleType_1439,MinPriceIncrementAmount_1146,"
            + "UserDefinedInstrument_9779,InstAttribType_871,InstAttribValue_872) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionfuture27NoInstAttrib "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,MaturityMonthYear_200,Currency_15,"
            + "SettlCurrency_120,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "MainFraction_37702,SubFraction_37703,PriceDisplayFormat_9800,"
            + "UnitOfMeasure_996,UnitOfMeasureQty_1147,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "DecayQuanity_5818,DecayStartDate_5819,OriginalContractSize_5849,"
            + "ContractMultiplier_231,ContractMultiplierUnit_1435,"
            + "FlowScheduleType_1439,MinPriceIncrementAmount_1146,"
            + "UserDefinedInstrument_9779,InstAttribType_871,InstAttribValue_872) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
    public DatabaseObject decodeNoLotTypeRules() 
    {
       final int template = 271; 
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
       
       colData.add("REPLAY ONLY"); //Used on replay feed only marketData.totNumReports()
       colData.add(marketData.securityUpdateAction());
       colData.add(marketData.lastUpdateTime());
       colData.add("REPLAY ONLY");//.append(marketData.mDSecurityTradingStatus());
       colData.add(marketData.applID());
       colData.add(marketData.marketSegmentID());
       colData.add(marketData.underlyingProduct());
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionFuture27.securityExchangeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionFuture27.securityGroupCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionFuture27.assetCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionFuture27.symbolCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       colData.add(marketData.securityID()); 
       
  try {
       colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
       colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionFuture27.securityTypeCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionFuture27.cFICodeCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       StringBuilder s = new StringBuilder();
       colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
       
  try {
       colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionFuture27.currencyCharacterEncoding().trim()));
       colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0),MDInstrumentDefinitionFuture27.settlCurrencyCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
  
       colData.add(marketData.matchAlgorithm());
       colData.add(marketData.minTradeVol());
       colData.add(marketData.maxTradeVol());
       colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
       colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
       colData.add(marketData.mainFraction());
       colData.add(marketData.subFraction());
       colData.add(marketData.priceDisplayFormat());
       
  try {
       colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionFuture27.unitOfMeasureCharacterEncoding().trim()));
      } 
  catch (UnsupportedEncodingException ex) 
      {
          System.out.println("UnsupportedEncodingException template 27" + ex);
      }
       colData.add(pf.formatPrice(marketData.unitOfMeasureQty().mantissa(), marketData.unitOfMeasureQty().exponent()));
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
       colData.add(marketData.decayQuantity());
       colData.add(marketData.decayStartDate());
       colData.add(marketData.originalContractSize());
       colData.add(marketData.contractMultiplier());
       colData.add(marketData.contractMultiplierUnit());
       colData.add(marketData.flowScheduleType());
       colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
       colData.add(marketData.userDefinedInstrument());
       
       for(final MDInstrumentDefinitionFuture27.NoEvents noEvents : marketData.noEvents())
       {
           noEvents.eventType();          
           noEvents.eventTime();
       }
       
       for(final MDInstrumentDefinitionFuture27.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
       {
           try 
           {          
               String na = new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionFuture27.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim());
           } 
           catch (UnsupportedEncodingException ex) 
           {
               System.out.println("UnsupportedEncodingException template 27" + ex);
           }
           noMDFeedTypes.marketDepth();
       }
       
       for(final MDInstrumentDefinitionFuture27.NoInstAttrib noInstAttrib : marketData.noInstAttrib())
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
       
       for(final MDInstrumentDefinitionFuture27.NoLotTypeRules noLotTypeRules : marketData.noLotTypeRules())
       {
           colData.add(noLotTypeRules.lotType());          
           colData.add(pf.formatPrice(noLotTypeRules.minLotSize().mantissa(), noLotTypeRules.minLotSize().exponent()));
           
       }
       
       if(option)
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionfuture27NoLotTypeRules "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,MaturityMonthYear_200,Currency_15,"
            + "SettlCurrency_120,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "MainFraction_37702,SubFraction_37703,PriceDisplayFormat_9800,"
            + "UnitOfMeasure_996,UnitOfMeasureQty_1147,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "DecayQuanity_5818,DecayStartDate_5819,OriginalContractSize_5849,"
            + "ContractMultiplier_231,ContractMultiplierUnit_1435,"
            + "FlowScheduleType_1439,MinPriceIncrementAmount_1146,"
            + "UserDefinedInstrument_9779,LotType_1093,LotSize_1231) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       else
       {
           tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionfuture27NoLotTypeRules "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,MaturityMonthYear_200,Currency_15,"
            + "SettlCurrency_120,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,DisplayFactor_9787,"
            + "MainFraction_37702,SubFraction_37703,PriceDisplayFormat_9800,"
            + "UnitOfMeasure_996,UnitOfMeasureQty_1147,TradeReferencePrice_1150,"
            + "SettlPriceType_731,OpenInterestQty_5792,ClearedVolume_5791,"
            + "HighLimitPrice_1149,LowLimitPrice_1148,MaxPriceVariation_1143,"
            + "DecayQuanity_5818,DecayStartDate_5819,OriginalContractSize_5849,"
            + "ContractMultiplier_231,ContractMultiplierUnit_1435,"
            + "FlowScheduleType_1439,MinPriceIncrementAmount_1146,"
            + "UserDefinedInstrument_9779,LotType_1093,LotSize_1231) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       }
       
       dbo.setTemplate(template);
       dbo.setTableName(tableName);
       dbo.setColData(colData);
       
       return dbo;
    }
    
}
