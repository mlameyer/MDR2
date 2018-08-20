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
import mktdata.MDInstrumentDefinitionOption41;
import mktdata.MatchEventIndicator;
import mktdata.SettlPriceType;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;
/**
 *
 * @author Administrator
 */
class Message41Decoder 
{
    private final boolean option;
    private String tableName;
    private final MDInstrumentDefinitionOption41 marketData = new MDInstrumentDefinitionOption41();
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
    
    public Message41Decoder(byte[] message, int bufferOffset, int actingBlockLength, int actingVersion, boolean option) 
    {
       this.message = message;
       this.bufferOffset = bufferOffset;
       this.actingBlockLength = actingBlockLength;
       this.actingVersion = actingVersion;
       this.option = option;
    }

    public DatabaseObject decode() 
    {
        final int template = 41;
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
        
        colData.add(marketData.totNumReports());
        colData.add(marketData.securityUpdateAction());
        colData.add(marketData.lastUpdateTime());
        colData.add(marketData.mDSecurityTradingStatus());
        colData.add(marketData.applID());
        colData.add(marketData.marketSegmentID());
        colData.add(marketData.underlyingProduct());
        try {
            colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionOption41.securityExchangeCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionOption41.securityGroupCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionOption41.assetCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionOption41.symbolCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.securityID()); 
        
        try {
                colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
                colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionOption41.securityTypeCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionOption41.cFICodeCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.putOrCall());
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
        
        try {
                colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionOption41.currencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.strikePrice().mantissa(), marketData.strikePrice().exponent()));
        
        try {
                colData.add(new String(message, 0, marketData.getStrikeCurrency(message, 0), MDInstrumentDefinitionOption41.strikeCurrencyCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0), MDInstrumentDefinitionOption41.settlCurrencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.minCabPrice().mantissa(), marketData.minCabPrice().exponent()));
        colData.add(marketData.matchAlgorithm());
        colData.add(marketData.minTradeVol());
        colData.add(marketData.maxTradeVol());
        colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
        colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
        colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
        colData.add(marketData.tickRule());
        colData.add(marketData.mainFraction());
        colData.add(marketData.subFraction());
        colData.add(marketData.priceDisplayFormat());
        
        try {
                colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionOption41.unitOfMeasureCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
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

        colData.add(marketData.clearedVolume());
        colData.add(marketData.openInterestQty());
        colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
        colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
        colData.add(marketData.userDefinedInstrument());
        
        for(final MDInstrumentDefinitionOption41.NoEvents noEvents : marketData.noEvents())
        {
            colData.add(noEvents.eventType());          
            colData.add(noEvents.eventTime());  
            
        }
       
        if(option)
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,EventType_865,EventTime_1145) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        else
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41 "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,EventType_865,EventTime_1145) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        dbo.setTemplate(template);
        dbo.setTableName(tableName);
        dbo.setColData(colData);

        return dbo;
    }
    
    public DatabaseObject decodeNoMDFeedTypes() 
    {
        final int template = 411;
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
        
        colData.add(marketData.totNumReports());
        colData.add(marketData.securityUpdateAction());
        colData.add(marketData.lastUpdateTime());
        colData.add(marketData.mDSecurityTradingStatus());
        colData.add(marketData.applID());
        colData.add(marketData.marketSegmentID());
        colData.add(marketData.underlyingProduct());
        try {
            colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionOption41.securityExchangeCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionOption41.securityGroupCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionOption41.assetCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionOption41.symbolCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.securityID()); 
        
        try {
                colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
                colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionOption41.securityTypeCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionOption41.cFICodeCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.putOrCall());
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
        
        try {
                colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionOption41.currencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.strikePrice().mantissa(), marketData.strikePrice().exponent()));
        
        try {
                colData.add(new String(message, 0, marketData.getStrikeCurrency(message, 0), MDInstrumentDefinitionOption41.strikeCurrencyCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0), MDInstrumentDefinitionOption41.settlCurrencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.minCabPrice().mantissa(), marketData.minCabPrice().exponent()));
        colData.add(marketData.matchAlgorithm());
        colData.add(marketData.minTradeVol());
        colData.add(marketData.maxTradeVol());
        colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
        colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
        colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
        colData.add(marketData.tickRule());
        colData.add(marketData.mainFraction());
        colData.add(marketData.subFraction());
        colData.add(marketData.priceDisplayFormat());
        
        try {
                colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionOption41.unitOfMeasureCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
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

        colData.add(marketData.clearedVolume());
        colData.add(marketData.openInterestQty());
        colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
        colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
        colData.add(marketData.userDefinedInstrument());
        
        for(final MDInstrumentDefinitionOption41.NoEvents noEvents : marketData.noEvents())
        {
            noEvents.eventType();          
            noEvents.eventTime();
        }
       
        for(final MDInstrumentDefinitionOption41.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
        {
            try 
            {          
                colData.add(new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionOption41.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim()));
            } 
            catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
            colData.add(noMDFeedTypes.marketDepth());
            
        }
       
        if(option)
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41NoMDFeedTypes "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,MDFeedType_1022,MarketDepth_264) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        else
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41NoMDFeedTypes "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,MDFeedType_1022,MarketDepth_264) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        dbo.setTemplate(template);
        dbo.setTableName(tableName);
        dbo.setColData(colData);

        return dbo;
    }
    
    public DatabaseObject decodeNoInstAttrib() 
    {
        final int template = 411;
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
        
        colData.add(marketData.totNumReports());
        colData.add(marketData.securityUpdateAction());
        colData.add(marketData.lastUpdateTime());
        colData.add(marketData.mDSecurityTradingStatus());
        colData.add(marketData.applID());
        colData.add(marketData.marketSegmentID());
        colData.add(marketData.underlyingProduct());
        try {
            colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionOption41.securityExchangeCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionOption41.securityGroupCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionOption41.assetCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionOption41.symbolCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.securityID()); 
        
        try {
                colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
                colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionOption41.securityTypeCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionOption41.cFICodeCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.putOrCall());
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
        
        try {
                colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionOption41.currencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.strikePrice().mantissa(), marketData.strikePrice().exponent()));
        
        try {
                colData.add(new String(message, 0, marketData.getStrikeCurrency(message, 0), MDInstrumentDefinitionOption41.strikeCurrencyCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0), MDInstrumentDefinitionOption41.settlCurrencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.minCabPrice().mantissa(), marketData.minCabPrice().exponent()));
        colData.add(marketData.matchAlgorithm());
        colData.add(marketData.minTradeVol());
        colData.add(marketData.maxTradeVol());
        colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
        colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
        colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
        colData.add(marketData.tickRule());
        colData.add(marketData.mainFraction());
        colData.add(marketData.subFraction());
        colData.add(marketData.priceDisplayFormat());
        
        try {
                colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionOption41.unitOfMeasureCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
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

        colData.add(marketData.clearedVolume());
        colData.add(marketData.openInterestQty());
        colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
        colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
        colData.add(marketData.userDefinedInstrument());
        
        for(final MDInstrumentDefinitionOption41.NoEvents noEvents : marketData.noEvents())
        {
            noEvents.eventType();          
            noEvents.eventTime();
        }
       
        for(final MDInstrumentDefinitionOption41.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
        {
            try 
            {          
                String na = new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionOption41.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim());
            } 
            catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
            noMDFeedTypes.marketDepth();
        }
       
        for(final MDInstrumentDefinitionOption41.NoInstAttrib noInstAttrib : marketData.noInstAttrib())
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
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41NoInstAttrib "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,InstAttribType_871,InstAttribValue_872) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        else
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41NoInstAttrib "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,InstAttribType_871,InstAttribValue_872) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        dbo.setTemplate(template);
        dbo.setTableName(tableName);
        dbo.setColData(colData);

        return dbo;
    }
    
    public DatabaseObject decodeNoLotTypeRules() 
    {
        final int template = 411;
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
        
        colData.add(marketData.totNumReports());
        colData.add(marketData.securityUpdateAction());
        colData.add(marketData.lastUpdateTime());
        colData.add(marketData.mDSecurityTradingStatus());
        colData.add(marketData.applID());
        colData.add(marketData.marketSegmentID());
        colData.add(marketData.underlyingProduct());
        try {
            colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionOption41.securityExchangeCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionOption41.securityGroupCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionOption41.assetCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionOption41.symbolCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.securityID()); 
        
        try {
                colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
                colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionOption41.securityTypeCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionOption41.cFICodeCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.putOrCall());
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
        
        try {
                colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionOption41.currencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.strikePrice().mantissa(), marketData.strikePrice().exponent()));
        
        try {
                colData.add(new String(message, 0, marketData.getStrikeCurrency(message, 0), MDInstrumentDefinitionOption41.strikeCurrencyCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0), MDInstrumentDefinitionOption41.settlCurrencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.minCabPrice().mantissa(), marketData.minCabPrice().exponent()));
        colData.add(marketData.matchAlgorithm());
        colData.add(marketData.minTradeVol());
        colData.add(marketData.maxTradeVol());
        colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
        colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
        colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
        colData.add(marketData.tickRule());
        colData.add(marketData.mainFraction());
        colData.add(marketData.subFraction());
        colData.add(marketData.priceDisplayFormat());
        
        try {
                colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionOption41.unitOfMeasureCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
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

        colData.add(marketData.clearedVolume());
        colData.add(marketData.openInterestQty());
        colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
        colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
        colData.add(marketData.userDefinedInstrument());
        
        for(final MDInstrumentDefinitionOption41.NoEvents noEvents : marketData.noEvents())
        {
            noEvents.eventType();          
            noEvents.eventTime();
        }
       
        for(final MDInstrumentDefinitionOption41.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
        {
            try 
            {          
                String na = new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionOption41.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim());
            } 
            catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
            noMDFeedTypes.marketDepth();
        }
       
        for(final MDInstrumentDefinitionOption41.NoInstAttrib noInstAttrib : marketData.noInstAttrib())
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
            d.append(IAV);
        }
       
        for(final MDInstrumentDefinitionOption41.NoLotTypeRules noLotTypeRules : marketData.noLotTypeRules())
        {
            colData.add(noLotTypeRules.lotType());          
            colData.add(pf.formatPrice(noLotTypeRules.minLotSize().mantissa(), noLotTypeRules.minLotSize().exponent())); 
            
        }
       
        if(option)
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41NoLotTypeRules "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,LotType_1093,MinLotSize_1231) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        else
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41NoLotTypeRules "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,LotType_1093,MinLotSize_1231) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        dbo.setTemplate(template);
        dbo.setTableName(tableName);
        dbo.setColData(colData);

        return dbo;
    }
    
    public DatabaseObject decodeNoUnderlyings() 
    {
        final int template = 412;
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
        
        colData.add(marketData.totNumReports());
        colData.add(marketData.securityUpdateAction());
        colData.add(marketData.lastUpdateTime());
        colData.add(marketData.mDSecurityTradingStatus());
        colData.add(marketData.applID());
        colData.add(marketData.marketSegmentID());
        colData.add(marketData.underlyingProduct());
        try {
            colData.add(new String(message, 0, marketData.getSecurityExchange(message, 0),MDInstrumentDefinitionOption41.securityExchangeCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSecurityGroup(message, 0),MDInstrumentDefinitionOption41.securityGroupCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getAsset(message, 0),MDInstrumentDefinitionOption41.assetCharacterEncoding().trim()));
            colData.add(new String(message, 0, marketData.getSymbol(message, 0),MDInstrumentDefinitionOption41.symbolCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.securityID()); 
        
        try {
                colData.add(new String(message, 0, marketData.getSecurityIDSource(message, 0, message.length)));
                colData.add(new String(message, 0, marketData.getSecurityType(message, 0),MDInstrumentDefinitionOption41.securityTypeCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getCFICode(message, 0), MDInstrumentDefinitionOption41.cFICodeCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
  
        colData.add(marketData.putOrCall());
        StringBuilder s = new StringBuilder();
        colData.add(s.append(marketData.maturityMonthYear().year()).append(marketData.maturityMonthYear().month()));
        
        try {
                colData.add(new String(message, 0, marketData.getCurrency(message, 0), MDInstrumentDefinitionOption41.currencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.strikePrice().mantissa(), marketData.strikePrice().exponent()));
        
        try {
                colData.add(new String(message, 0, marketData.getStrikeCurrency(message, 0), MDInstrumentDefinitionOption41.strikeCurrencyCharacterEncoding().trim()));
                colData.add(new String(message, 0, marketData.getSettlCurrency(message, 0), MDInstrumentDefinitionOption41.settlCurrencyCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
        
        colData.add(pf.formatPrice(marketData.minCabPrice().mantissa(), marketData.minCabPrice().exponent()));
        colData.add(marketData.matchAlgorithm());
        colData.add(marketData.minTradeVol());
        colData.add(marketData.maxTradeVol());
        colData.add(pf.formatPrice(marketData.minPriceIncrement().mantissa(), marketData.minPriceIncrement().exponent()));
        colData.add(pf.formatPrice(marketData.minPriceIncrementAmount().mantissa(), marketData.minPriceIncrementAmount().exponent()));
        colData.add(pf.formatPrice(marketData.displayFactor().mantissa(), marketData.displayFactor().exponent()));
        colData.add(marketData.tickRule());
        colData.add(marketData.mainFraction());
        colData.add(marketData.subFraction());
        colData.add(marketData.priceDisplayFormat());
        
        try {
                colData.add(new String(message, 0, marketData.getUnitOfMeasure(message, 0),MDInstrumentDefinitionOption41.unitOfMeasureCharacterEncoding().trim()));
            } 
        catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
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

        colData.add(marketData.clearedVolume());
        colData.add(marketData.openInterestQty());
        colData.add(pf.formatPrice(marketData.highLimitPrice().mantissa(), marketData.highLimitPrice().exponent()));
        colData.add(pf.formatPrice(marketData.lowLimitPrice().mantissa(), marketData.lowLimitPrice().exponent())); 
        colData.add(marketData.userDefinedInstrument());
        
        for(final MDInstrumentDefinitionOption41.NoEvents noEvents : marketData.noEvents())
        {
            noEvents.eventType();          
            noEvents.eventTime();
        }
       
        for(final MDInstrumentDefinitionOption41.NoMDFeedTypes noMDFeedTypes : marketData.noMDFeedTypes())
        {
            try 
            {          
                String na = new String(message, 0, noMDFeedTypes.getMDFeedType(message, 0),MDInstrumentDefinitionOption41.NoMDFeedTypes.mDFeedTypeCharacterEncoding().trim());
            } 
            catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
            
            noMDFeedTypes.marketDepth();
        }
       
        for(final MDInstrumentDefinitionOption41.NoInstAttrib noInstAttrib : marketData.noInstAttrib())
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
            d.append(IAV);
        }
       
        for(final MDInstrumentDefinitionOption41.NoLotTypeRules noLotTypeRules : marketData.noLotTypeRules())
        {
            noLotTypeRules.lotType();          
            pf.formatPrice(noLotTypeRules.minLotSize().mantissa(), noLotTypeRules.minLotSize().exponent());
        }
        
        for(final MDInstrumentDefinitionOption41.NoUnderlyings noUnderlyings : marketData.noUnderlyings())
        {
            colData.add(noUnderlyings.underlyingSecurityID());         
            colData.add(noUnderlyings.getUnderlyingSecurityIDSource(message, 0, message.length));  
            try 
            {          
                colData.add(new String(message, 0, noUnderlyings.getUnderlyingSymbol(message, 0),MDInstrumentDefinitionOption41.NoUnderlyings.underlyingSymbolCharacterEncoding().trim()));
            } 
            catch (UnsupportedEncodingException ex) 
            {
                System.out.println("UnsupportedEncodingException template 41" + ex);
            }
            
        }
       
        if(option)
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41NoUnderlyings "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,UnderlyingSecurityID_309,"
            + "UnderlyingSecurityIDSource_305,UnderlyingSymbol_311) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        else
        {
            tableName = "INSERT INTO "
            + "cme_market_datafeed_b.mdinstrumentdefinitionoption41NoUnderlyings "
            + "(Seq_Num_34,Sending_Time_52,TempId,MsgType_35,MatchEventIndicator_5799,"
            + "TotNumReports_911,SecurityUpdateAction_980,LastUpdateTime_779,"
            + "MDSecurityTradingStatus_1682,ApplID_1180,MarketSegmentID_1300,"
            + "UnderlyingProduct_462,SecurityExchange_207,SecurityGroup_1151,"
            + "Asset_6937,Symbol_55,SecurityId_48,SecurityIDSource_22,"
            + "SecurityType_167,CFICode_461,PutOrCall_201,MaturityMonthYear_200,"
            + "Currency_15,StrikePrice_202,StrikeCurrency_947,SettlCurrency_120,"
            + "MinCabPrice_9850,MatchAlgorithm_1142,MinTradeVol_562,"
            + "MaxTradeVol_1140,MinPriceIncrement_969,MinPriceIncrementAmount_1146,"
            + "DisplayFactor_9787,TickRule_6350,MainFraction_37702,SubFraction_37703,"
            + "PriceDisplayFormat_9800,UnitOfMeasure_996,UnitOfMeasureQty_1147,"
            + "TradeReferencePrice_1150,SettlPriceType_731,ClearedVolume_5791,"
            + "OpenInterestQty_5792,LowLimitPrice_1148,HighLimitPrice_1149,"
            + "UserDefinedInstrument_9779,UnderlyingSecurityID_309,"
            + "UnderlyingSecurityIDSource_305,UnderlyingSymbol_311) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        dbo.setTemplate(template);
        dbo.setTableName(tableName);
        dbo.setColData(colData);

        return dbo;
    }
    
}
