package com.orientechnologies.orient.core.storage.impl.local.paginated.wal.pageoperations.cluster.clusterpage;

import com.orientechnologies.common.serialization.types.OIntegerSerializer;
import com.orientechnologies.orient.core.storage.cache.OCacheEntry;
import com.orientechnologies.orient.core.storage.cluster.OClusterPage;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OPageOperationRecord;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.WALRecordTypes;

import java.nio.ByteBuffer;

public final class OClusterPageDeleteRecordOperation extends OPageOperationRecord {
  private int    index;
  private int    recordVersion;
  private byte[] record;

  public OClusterPageDeleteRecordOperation() {
  }

  public OClusterPageDeleteRecordOperation(int index, int recordVersion, byte[] record) {
    this.index = index;
    this.recordVersion = recordVersion;
    this.record = record;
  }

  public int getIndex() {
    return index;
  }

  public int getRecordVersion() {
    return recordVersion;
  }

  public byte[] getRecord() {
    return record;
  }

  @Override
  protected void doRedo(OCacheEntry cacheEntry) {
    final OClusterPage clusterPage = new OClusterPage(cacheEntry, false);
    clusterPage.deleteRecord(index);
  }

  @Override
  protected void doUndo(OCacheEntry cacheEntry) {
    final OClusterPage clusterPage = new OClusterPage(cacheEntry, false);
    final int pos = clusterPage.appendRecord(recordVersion, record);
    assert pos == index;
  }

  @Override
  public boolean isUpdateMasterRecord() {
    return false;
  }

  @Override
  public byte getId() {
    return WALRecordTypes.CLUSTER_PAGE_DELETE_RECORD;
  }

  @Override
  public int toStream(byte[] content, int offset) {
    offset = super.toStream(content, offset);

    OIntegerSerializer.INSTANCE.serializeNative(index, content, offset);
    offset += OIntegerSerializer.INT_SIZE;

    OIntegerSerializer.INSTANCE.serializeNative(recordVersion, content, offset);
    offset += OIntegerSerializer.INT_SIZE;

    OIntegerSerializer.INSTANCE.serializeNative(record.length, content, offset);
    offset += OIntegerSerializer.INT_SIZE;

    System.arraycopy(record, 0, content, offset, record.length);
    offset += record.length;

    return offset;
  }

  @Override
  public void toStream(ByteBuffer buffer) {
    super.toStream(buffer);

    buffer.putInt(index);
    buffer.putInt(recordVersion);

    buffer.putInt(record.length);
    buffer.put(record);
  }

  @Override
  public int fromStream(byte[] content, int offset) {
    offset = super.fromStream(content, offset);

    index = OIntegerSerializer.INSTANCE.deserializeNative(content, offset);
    offset += OIntegerSerializer.INT_SIZE;

    recordVersion = OIntegerSerializer.INSTANCE.deserializeNative(content, offset);
    offset += OIntegerSerializer.INT_SIZE;

    final int recordLen = OIntegerSerializer.INSTANCE.deserializeNative(content, offset);
    offset += OIntegerSerializer.INT_SIZE;

    record = new byte[recordLen];
    System.arraycopy(content, offset, record, 0, recordLen);
    offset += recordLen;

    return offset;
  }

  @Override
  public int serializedSize() {
    return super.serializedSize() + 3 * OIntegerSerializer.INT_SIZE + record.length;
  }
}
