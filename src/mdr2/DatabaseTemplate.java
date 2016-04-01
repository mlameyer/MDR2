/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.util.Properties;
import javax.swing.JTextArea;
import database.*;

/**
 *
 * @author Administrator
 */
class DatabaseTemplate 
{
    private final Properties prop;
    private final JTextArea screen;
    private final boolean option;
    
    public DatabaseTemplate(Properties prop, JTextArea screen, boolean option) 
    {
        this.prop = prop;
        this.screen = screen;
        this.option = option;
    }
    
    public void template(DatabaseObject object)
    {
        int template = object.getTemplate();
        
        switch(template)
        {
            case 4:Template4 tmp4 = new Template4(object.getTableName(), object.getColData(), prop, option);
                    tmp4.execute();
                break;
            
            case 12:Template12 tmp12 = new Template12(object.getTableName(), object.getColData(), prop, option);
                    tmp12.execute();
                break;
            
            case 15:Template15 tmp15 = new Template15(object.getTableName(), object.getColData(), prop, option);
                    tmp15.execute();
                break;
                
            case 16:Template16 tmp16 = new Template16(object.getTableName(), object.getColData(), prop, option);
                    tmp16.execute();
                break; 
                
            case 27:Template27 tmp27 = new Template27(object.getTableName(), object.getColData(), prop, option);
                    tmp27.execute();
                break;
                
            case 271:Template271 tmp271 = new Template271(object.getTableName(), object.getColData(), prop);
                    tmp271.execute();
                break;
                
            case 29:Template29 tmp29 = new Template29(object.getTableName(), object.getColData(), prop, option);
                    tmp29.execute();
                break;
                
            case 291:Template291 tmp291 = new Template291(object.getTableName(), object.getColData(), prop);
                    tmp291.execute();
                break;
                
            case 292:Template292 tmp292 = new Template292(object.getTableName(), object.getColData(), prop);
                    tmp292.execute();
                break;
                
            case 30:Template30 tmp30 = new Template30(object.getTableName(), object.getColData(), prop, option);
                    tmp30.execute();
                break;
                
            case 32:Template32 tmp32 = new Template32(object.getTableName(), object.getColData(), prop, option);
                    tmp32.execute();
                break;
                
            case 33:Template33 tmp33 = new Template33(object.getTableName(), object.getColData(), prop, option);
                    tmp33.execute();
                break;
                
            case 34:Template34 tmp34 = new Template34(object.getTableName(), object.getColData(), prop, option);
                    tmp34.execute();
                break;
                
            case 35:Template35 tmp35 = new Template35(object.getTableName(), object.getColData(), prop, option);
                    tmp35.execute();
                break;
                
            case 36:Template36 tmp36 = new Template36(object.getTableName(), object.getColData(), prop, option);
                    tmp36.execute();
                break;
                
            case 37:Template37 tmp37 = new Template37(object.getTableName(), object.getColData(), prop, option);
                    tmp37.execute();
                break;
                
            case 38:Template38 tmp38 = new Template38(object.getTableName(), object.getColData(), prop, option);
                    tmp38.execute();
                break;
                
            case 39:Template39 tmp39 = new Template39(object.getTableName(), object.getColData(), prop, option);
                    tmp39.execute();
                break;
                
            case 41:Template41 tmp41 = new Template41(object.getTableName(), object.getColData(), prop, option);
                    tmp41.execute();
                break;
                
            case 411:Template411 tmp411 = new Template411(object.getTableName(), object.getColData(), prop);
                    tmp411.execute();
                break;
                
            case 412:Template412 tmp412 = new Template412(object.getTableName(), object.getColData(), prop);
                    tmp412.execute();
                break;
               
            case 42:Template42 tmp42 = new Template42(object.getTableName(), object.getColData(), prop, option);
                    tmp42.execute();
                break;
                
            case 421:Template421 tmp421 = new Template421(object.getTableName(), object.getColData(), prop);
                    tmp421.execute();
                break;
        }
    }
}
