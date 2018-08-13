/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mdr2;

import java.util.LinkedList;
import mktdata.MessageHeader;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

/**
 *
 * @author Administrator
 */
class DecodeMessage 
{
    private final MessageHeader MESSAGE_HEADER = new MessageHeader();
    private final LinkedList<DatabaseObject> decodedData;
    private final boolean option;
    private Message4Decoder msg4; 
    private Message12Decoder msg12; 
    private Message15Decoder msg15; 
    private Message16Decoder msg16; 
    private Message27Decoder msg27; 
    private Message29Decoder msg29; 
    private Message30Decoder msg30; 
    private Message32Decoder msg32; 
    private Message33Decoder msg33; 
    private Message34Decoder msg34; 
    private Message35Decoder msg35; 
    private Message36Decoder msg36; 
    private Message37Decoder msg37; 
    private Message38Decoder msg38; 
    private Message39Decoder msg39; 
    private Message41Decoder msg41; 
    private Message42Decoder msg42; 
    private Message46Decoder msg46;
    private Message48Decoder msg48;
    private Message49Decoder msg49;
    private Message50Decoder msg50;
    private Message51Decoder msg51;
    private Message52Decoder msg52;
    private Message54Decoder msg54;
    private Message55Decoder msg55;
    private Message56Decoder msg56;

    public DecodeMessage(LinkedList<DatabaseObject> decodedData, boolean option) 
    {
        this.decodedData = decodedData;
        this.option = option;
    }
    
    public void decode(byte[] message) 
    {
        final DirectBuffer directBuffer = new DirectBuffer(message);
        final short messageTemplateVersion = 1;
        final int templateId, actingBlockLength, schemaId, actingVersion;
        int bufferOffset = 14;
        
        MESSAGE_HEADER.wrap(directBuffer, bufferOffset, messageTemplateVersion);
        
        templateId = MESSAGE_HEADER.templateId();
        actingBlockLength = MESSAGE_HEADER.blockLength();
        schemaId = MESSAGE_HEADER.schemaId();
        actingVersion = MESSAGE_HEADER.version();
        
        bufferOffset += MESSAGE_HEADER.size();
              
        switch(templateId){
            case 4: 
                
                msg4 = new Message4Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg4.decode());
                break;
            
            case 12: 
                
                msg12 = new Message12Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg12.decode());
                break;
            
            case 15:
                
                msg15 = new Message15Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg15.decode());
                break;
                
            case 16: 
                
                msg16 = new Message16Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg16.decode());
                break;
                
            case 27: 
                
                msg27 = new Message27Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg27.decodeNoEvents());
                decodedData.add(msg27.decodeNoMDFeedTypes());
                decodedData.add(msg27.decodeNoInstAttrib());
                decodedData.add(msg27.decodeNoLotTypeRules());
                break;
                
            case 29: 
                
                msg29 = new Message29Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg29.decodeNoEvents());
                decodedData.add(msg29.decodeNoMDFeedTypes());
                decodedData.add(msg29.decodeNoInstAttrib());
                decodedData.add(msg29.decodeNoLotTypeRules());
                decodedData.add(msg29.decodeNoLegs());
                break;
                
            case 30: 
                
                msg30 = new Message30Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg30.decode());
                break;
               
            case 32: 
                
                msg32 = new Message32Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg32.decode());
                break;
                
            case 33: 
                
                msg33 = new Message33Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg33.decode());
                break;
                
            case 34: 
                
                msg34 = new Message34Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg34.decode());
                break;
                
            case 35: 
                
                msg35 = new Message35Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg35.decode());
                break;
                
            case 36: 
                
                msg36 = new Message36Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg36.decode());
                break;
                
            case 37: 
                
                msg37 = new Message37Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg37.decode());
                break;
                
            case 38: 
                
                msg38 = new Message38Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg38.decode());
                break;
                
            case 39: 
                
                msg39 = new Message39Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg39.decode());
                break;
                
            case 41: 
                
                msg41 = new Message41Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg41.decode());
                decodedData.add(msg41.decodeNoMDFeedTypes());
                decodedData.add(msg41.decodeNoInstAttrib());
                decodedData.add(msg41.decodeNoLotTypeRules());
                decodedData.add(msg41.decodeNoUnderlyings());
                break;
                
            case 42: 
                
                msg42 = new Message42Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg42.decodeNoMDEntries());
                decodedData.add(msg42.decodeNoOrderIDEntries());
                break;
                
            case 46: 
                
                msg46 = new Message46Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg46.decode());
                break;
            
            case 48: 
                
                msg48 = new Message48Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg48.decodeNoMDEntries());
                decodedData.add(msg48.decodeNoOrderIDEntries());
                break;
                
            case 49: 
                
                msg49 = new Message49Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg49.decode());
                break;
            
            case 50: 
                
                msg50 = new Message50Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg50.decode());
                break;
                
            case 51: 
                
                msg51 = new Message51Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg51.decode());
                break;
            
            case 52: 
                
                msg52 = new Message52Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg52.decode());
                break;
                
            case 54: 
                
                msg54 = new Message54Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg54.decodeNoEvents());
                decodedData.add(msg54.decodeNoMDFeedTypes());
                decodedData.add(msg54.decodeNoInstAttrib());
                decodedData.add(msg54.decodeNoLotTypeRules());
                break;
            
            case 55: 
                
                msg55 = new Message55Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg55.decode());
                decodedData.add(msg55.decodeNoMDFeedTypes());
                decodedData.add(msg55.decodeNoInstAttrib());
                decodedData.add(msg55.decodeNoLotTypeRules());
                decodedData.add(msg55.decodeNoUnderlyings());
                break;
                
            case 56: 
                
                msg56 = new Message56Decoder(message, bufferOffset, actingBlockLength, actingVersion, option);
                decodedData.add(msg56.decodeNoEvents());
                decodedData.add(msg56.decodeNoMDFeedTypes());
                decodedData.add(msg56.decodeNoInstAttrib());
                decodedData.add(msg56.decodeNoLotTypeRules());
                decodedData.add(msg56.decodeNoLegs());
                break;
        }
        
    }
    
}