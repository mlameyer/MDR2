/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class Template271 
{
    private final String tableName;
    private final ArrayList colData;
    private final Properties prop;

    public Template271(String tableName, ArrayList colData, Properties prop) 
    {
        this.tableName = tableName;
        this.colData = colData;
        this.prop = prop;
    }
    
    public void execute()
    {
        GetConnection connect = new GetConnection();
        connect.setJDBC_CONNECTION_URL(prop.getProperty("JDBC_CONNECTION_URL"));
        connect.setMySQLDB_URL(prop.getProperty("MySQLDB_URL"));
        connect.setMySQLUser(prop.getProperty("MySQLUser"));
        connect.setMySQLPassword(prop.getProperty("MySQLPassword"));
        BigDecimal tag52 = null, tag779 = null;
        
        Connection conn = connect.getConnection();
        PreparedStatement stmt = null;
        UnixtoStandardTime time = new UnixtoStandardTime();
        
        try
        {
            tag52 = time.convert(colData.get(1).toString().trim());
            tag779 = time.convert(colData.get(7).toString().trim());
        }
        catch(java.lang.StringIndexOutOfBoundsException ex)
        {
            System.out.println("Class database.Template27 error: " + ex);
        }
        
        try 
        {
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(tableName);

            for(int i = 48; i < colData.size(); i = i + 2)
            {
                stmt.setInt(1, Integer.parseInt(colData.get(0).toString().trim()));//Seq_Num_34
                stmt.setBigDecimal(2, tag52);//Sending_Time_52
                stmt.setInt(3, Integer.parseInt(colData.get(2).toString().trim()));//TempId
                stmt.setString(4, colData.get(3).toString().trim());//MsgType_35
                stmt.setString(5, colData.get(4).toString().trim());//MatchEventIndicator_5799
                stmt.setString(6, colData.get(5).toString().trim());//TotNumReports_911
                stmt.setString(7, colData.get(6).toString().trim());//SecurityUpdateAction_980
                stmt.setBigDecimal(8, tag779);//LastUpdateTime_779
                stmt.setString(9, colData.get(8).toString().trim());//MDSecurityTradingStatus_1682
                stmt.setInt(10, Integer.parseInt(colData.get(9).toString().trim()));//ApplID_1180
                stmt.setInt(11, Integer.parseInt(colData.get(10).toString().trim()));//MarketSegmentID_1300
                stmt.setInt(12, Integer.parseInt(colData.get(11).toString().trim()));//UnderlyingProduct_462
                stmt.setString(13, colData.get(12).toString().trim());//SecurityExchange_207
                stmt.setString(14, colData.get(13).toString().trim());//SecurityGroup_1151
                stmt.setString(15, colData.get(14).toString().trim());//Asset_6937
                stmt.setString(16, colData.get(15).toString().trim());//Symbol_55
                stmt.setInt(17, Integer.parseInt(colData.get(16).toString().trim()));//SecurityId_48
                stmt.setString(18, colData.get(17).toString().trim());//SecurityIDSource_22
                stmt.setString(19, colData.get(18).toString().trim());//SecurityType_167
                stmt.setString(20, colData.get(19).toString().trim());//CFICode_461
                stmt.setInt(21, Integer.parseInt(colData.get(20).toString().trim()));//MaturityMonthYear_200
                stmt.setString(22, colData.get(21).toString().trim());//Currency_15
                stmt.setString(23, colData.get(22).toString().trim());//SettlCurrency_120
                stmt.setInt(24, Integer.parseInt(colData.get(23).toString().trim()));//MatchAlgorithm_1142
                stmt.setInt(25, Integer.parseInt(colData.get(24).toString().trim()));//MinTradeVol_562
                stmt.setInt(26, Integer.parseInt(colData.get(25).toString().trim()));//MaxTradeVol_1140
                stmt.setFloat(27, Float.parseFloat(colData.get(26).toString().trim()));//MinPriceIncrement_969
                stmt.setFloat(28, Float.parseFloat(colData.get(27).toString().trim()));//DisplayFactor_9787
                stmt.setInt(29, Integer.parseInt(colData.get(28).toString().trim()));//MainFraction_37702
                stmt.setInt(30, Integer.parseInt(colData.get(29).toString().trim()));//SubFraction_37703
                stmt.setInt(31, Integer.parseInt(colData.get(30).toString().trim()));//PriceDisplayFormat_9800
                stmt.setString(32, colData.get(31).toString().trim());//UnitOfMeasure_996
                stmt.setFloat(33, Float.parseFloat(colData.get(32).toString().trim()));//UnitOfMeasureQty_1147
                stmt.setFloat(34, Float.parseFloat(colData.get(33).toString().trim()));//TradeReferencePrice_1150
                stmt.setString(35, colData.get(34).toString().trim());//SettlPriceType_731
                stmt.setInt(36, Integer.parseInt(colData.get(35).toString().trim()));//OpenInterestQty_5792
                stmt.setInt(37, Integer.parseInt(colData.get(36).toString().trim()));//ClearedVolume_5791
                stmt.setFloat(38, Float.parseFloat(colData.get(37).toString().trim()));//HighLimitPrice_1149
                stmt.setFloat(39, Float.parseFloat(colData.get(38).toString().trim()));//LowLimitPrice_1148
                stmt.setFloat(40, Float.parseFloat(colData.get(39).toString().trim()));//MaxPriceVariation_1143
                stmt.setInt(41, Integer.parseInt(colData.get(40).toString().trim()));//DecayQuanity_5818
                stmt.setInt(42, Integer.parseInt(colData.get(41).toString().trim()));//DecayStartDate_5819
                stmt.setInt(43, Integer.parseInt(colData.get(42).toString().trim()));//OriginalContractSize_5849
                stmt.setInt(44, Integer.parseInt(colData.get(43).toString().trim()));//ContractMultiplier_231
                stmt.setInt(45, Integer.parseInt(colData.get(44).toString().trim()));//ContractMultiplierUnit_1435
                stmt.setInt(46, Integer.parseInt(colData.get(45).toString().trim()));//FlowScheduleType_1439
                stmt.setFloat(47, Float.parseFloat(colData.get(46).toString().trim()));//MinPriceIncrementAmount_1146
                stmt.setString(48, colData.get(47).toString().trim());//UserDefinedInstrument_9779
                
                if(colData.get(i + 0) == null)//
                {
                    stmt.setInt(49, java.sql.Types.INTEGER);//upaction
                    stmt.setLong(50, java.sql.Types.BIGINT);//entrytype
                }
                else
                {
                    stmt.setString(49, colData.get(i + 0).toString().trim());
                    stmt.setString(50, colData.get(i + 1).toString().trim());
                }
                
                stmt.addBatch();
                //System.out.println(stmt);
            }
            
            stmt.executeBatch();
            conn.commit();
        } 
        catch (SQLException ex) 
        {
            System.out.println("Class database.Template271 error: " + ex);
        }
        finally
        {
            try 
            {
                stmt.close();
                conn.close();
            } 
            catch (SQLException ex) 
            {
                System.out.println("Class database.Template271 error: " + ex);
            }
        }
    }
}
