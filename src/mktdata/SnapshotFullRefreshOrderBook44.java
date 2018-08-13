/* Generated SBE (Simple Binary Encoding) message codec */
package mktdata;

import uk.co.real_logic.sbe.codec.java.*;

public class SnapshotFullRefreshOrderBook44
{
    public static final int BLOCK_LENGTH = 28;
    public static final int TEMPLATE_ID = 44;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 9;

    private final SnapshotFullRefreshOrderBook44 parentMessage = this;
    private DirectBuffer buffer;
    private int offset;
    private int limit;
    private int actingBlockLength;
    private int actingVersion;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "W";
    }

    public int offset()
    {
        return offset;
    }

    public SnapshotFullRefreshOrderBook44 wrapForEncode(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = BLOCK_LENGTH;
        this.actingVersion = SCHEMA_VERSION;
        limit(offset + actingBlockLength);

        return this;
    }

    public SnapshotFullRefreshOrderBook44 wrapForDecode(
        final DirectBuffer buffer, final int offset, final int actingBlockLength, final int actingVersion)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public int size()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        buffer.checkLimit(limit);
        this.limit = limit;
    }

    public static int LastMsgSeqNumProcessedId()
    {
        return 369;
    }

    public static String LastMsgSeqNumProcessedMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "SeqNum";
        }

        return "";
    }

    public static long lastMsgSeqNumProcessedNullValue()
    {
        return 4294967294L;
    }

    public static long lastMsgSeqNumProcessedMinValue()
    {
        return 0L;
    }

    public static long lastMsgSeqNumProcessedMaxValue()
    {
        return 4294967293L;
    }

    public long lastMsgSeqNumProcessed()
    {
        return CodecUtil.uint32Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public SnapshotFullRefreshOrderBook44 lastMsgSeqNumProcessed(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int TotNumReportsId()
    {
        return 911;
    }

    public static String TotNumReportsMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "int";
        }

        return "";
    }

    public static long totNumReportsNullValue()
    {
        return 4294967294L;
    }

    public static long totNumReportsMinValue()
    {
        return 0L;
    }

    public static long totNumReportsMaxValue()
    {
        return 4294967293L;
    }

    public long totNumReports()
    {
        return CodecUtil.uint32Get(buffer, offset + 4, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public SnapshotFullRefreshOrderBook44 totNumReports(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int SecurityIDId()
    {
        return 48;
    }

    public static String SecurityIDMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "String";
        }

        return "";
    }

    public static int securityIDNullValue()
    {
        return -2147483648;
    }

    public static int securityIDMinValue()
    {
        return -2147483647;
    }

    public static int securityIDMaxValue()
    {
        return 2147483647;
    }

    public int securityID()
    {
        return CodecUtil.int32Get(buffer, offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public SnapshotFullRefreshOrderBook44 securityID(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int NoChunksId()
    {
        return 37709;
    }

    public static String NoChunksMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "int";
        }

        return "";
    }

    public static long noChunksNullValue()
    {
        return 4294967294L;
    }

    public static long noChunksMinValue()
    {
        return 0L;
    }

    public static long noChunksMaxValue()
    {
        return 4294967293L;
    }

    public long noChunks()
    {
        return CodecUtil.uint32Get(buffer, offset + 12, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public SnapshotFullRefreshOrderBook44 noChunks(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 12, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int CurrentChunkId()
    {
        return 37710;
    }

    public static String CurrentChunkMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "int";
        }

        return "";
    }

    public static long currentChunkNullValue()
    {
        return 4294967294L;
    }

    public static long currentChunkMinValue()
    {
        return 0L;
    }

    public static long currentChunkMaxValue()
    {
        return 4294967293L;
    }

    public long currentChunk()
    {
        return CodecUtil.uint32Get(buffer, offset + 16, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public SnapshotFullRefreshOrderBook44 currentChunk(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 16, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int TransactTimeId()
    {
        return 60;
    }

    public static String TransactTimeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "UTCTimestamp";
        }

        return "";
    }

    public static long transactTimeNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long transactTimeMinValue()
    {
        return 0x0L;
    }

    public static long transactTimeMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public long transactTime()
    {
        return CodecUtil.uint64Get(buffer, offset + 20, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public SnapshotFullRefreshOrderBook44 transactTime(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 20, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    private final NoMDEntries noMDEntries = new NoMDEntries();

    public static long NoMDEntriesId()
    {
        return 268;
    }

    public NoMDEntries noMDEntries()
    {
        noMDEntries.wrapForDecode(parentMessage, buffer, actingVersion);
        return noMDEntries;
    }

    public NoMDEntries noMDEntriesCount(final int count)
    {
        noMDEntries.wrapForEncode(parentMessage, buffer, count);
        return noMDEntries;
    }

    public static class NoMDEntries implements Iterable<NoMDEntries>, java.util.Iterator<NoMDEntries>
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSize dimensions = new GroupSize();
        private SnapshotFullRefreshOrderBook44 parentMessage;
        private DirectBuffer buffer;
        private int blockLength;
        private int actingVersion;
        private int count;
        private int index;
        private int offset;

        public void wrapForDecode(
            final SnapshotFullRefreshOrderBook44 parentMessage, final DirectBuffer buffer, final int actingVersion)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit(), actingVersion);
            blockLength = dimensions.blockLength();
            count = dimensions.numInGroup();
            this.actingVersion = actingVersion;
            index = -1;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public void wrapForEncode(final SnapshotFullRefreshOrderBook44 parentMessage, final DirectBuffer buffer, final int count)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            actingVersion = SCHEMA_VERSION;
            dimensions.wrap(buffer, parentMessage.limit(), actingVersion);
            dimensions.blockLength((int)29);
            dimensions.numInGroup((short)count);
            index = -1;
            this.count = count;
            blockLength = 29;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public static int sbeHeaderSize()
        {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength()
        {
            return 29;
        }

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int count()
        {
            return count;
        }

        @Override
        public java.util.Iterator<NoMDEntries> iterator()
        {
            return this;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext()
        {
            return (index + 1) < count;
        }

        @Override
        public NoMDEntries next()
        {
            if (index + 1 >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
        }

        public static int OrderIDId()
        {
            return 37;
        }

        public static String OrderIDMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static long orderIDNullValue()
        {
            return 0xffffffffffffffffL;
        }

        public static long orderIDMinValue()
        {
            return 0x0L;
        }

        public static long orderIDMaxValue()
        {
            return 0xfffffffffffffffeL;
        }

        public long orderID()
        {
            return CodecUtil.uint64Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public NoMDEntries orderID(final long value)
        {
            CodecUtil.uint64Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }

        public static int MDOrderPriorityId()
        {
            return 37707;
        }

        public static String MDOrderPriorityMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static long mDOrderPriorityNullValue()
        {
            return 0xffffffffffffffffL;
        }

        public static long mDOrderPriorityMinValue()
        {
            return 0x0L;
        }

        public static long mDOrderPriorityMaxValue()
        {
            return 0xfffffffffffffffeL;
        }

        public long mDOrderPriority()
        {
            return CodecUtil.uint64Get(buffer, offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public NoMDEntries mDOrderPriority(final long value)
        {
            CodecUtil.uint64Put(buffer, offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }

        public static int MDEntryPxId()
        {
            return 270;
        }

        public static String MDEntryPxMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Price";
            }

            return "";
        }

        private final PRICE mDEntryPx = new PRICE();

        public PRICE mDEntryPx()
        {
            mDEntryPx.wrap(buffer, offset + 16, actingVersion);
            return mDEntryPx;
        }

        public static int MDDisplayQtyId()
        {
            return 37706;
        }

        public static String MDDisplayQtyMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Qty";
            }

            return "";
        }

        public static int mDDisplayQtyNullValue()
        {
            return -2147483648;
        }

        public static int mDDisplayQtyMinValue()
        {
            return -2147483647;
        }

        public static int mDDisplayQtyMaxValue()
        {
            return 2147483647;
        }

        public int mDDisplayQty()
        {
            return CodecUtil.int32Get(buffer, offset + 24, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public NoMDEntries mDDisplayQty(final int value)
        {
            CodecUtil.int32Put(buffer, offset + 24, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }

        public static int MDEntryTypeId()
        {
            return 269;
        }

        public static String MDEntryTypeMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "char";
            }

            return "";
        }

        public MDEntryTypeBook mDEntryType()
        {
            return MDEntryTypeBook.get(CodecUtil.charGet(buffer, offset + 28));
        }

        public NoMDEntries mDEntryType(final MDEntryTypeBook value)
        {
            CodecUtil.charPut(buffer, offset + 28, value.value());
            return this;
        }
    }
}
