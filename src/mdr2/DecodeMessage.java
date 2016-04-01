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
        }
        
    }
    
}