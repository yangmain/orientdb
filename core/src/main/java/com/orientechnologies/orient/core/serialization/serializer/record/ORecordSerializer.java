/*
 *
 *  *  Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  *
 *  * For more information: http://orientdb.com
 *
 */

package com.orientechnologies.orient.core.serialization.serializer.record;

import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;

public interface ORecordSerializer {
  ORecord fromStream(byte[] iSource, ORecord iRecord, String[] iFields);

  byte[] toStream(ORecord iSource, boolean iOnlyDelta);

  byte[] writeClassOnly(ORecord iSource);

  int getCurrentVersion();

  int getMinSupportedVersion();

  String[] getFieldNamesRoot(ODocument reference, byte[] iSource);  
  String[] getFieldNamesEmbedded(ODocument reference, byte[] iSource, int offset, int serializerVersion);

  boolean getSupportBinaryEvaluate();

  String getName();
  
  <RET> RET deserializeFieldFromRoot(byte[]record, String iFieldName);
  <RET> RET deserializeFieldFromEmbedded(byte[]record, int offsetOfDocumentInBytes, String iFieldName, int serializerVersion);
}
