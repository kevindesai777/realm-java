/*
 * Copyright 2017 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.realm.internal;


import io.realm.RealmFieldType;


public class OsObjectSchemaInfo implements NativeObject {
    private long nativePtr;
    private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();

    /**
     * Creates a schema object using object store. This constructor is intended to be used by
     * the validation of schema, object schemas and properties through the object store. Even though the constructor
     * is public, there is never a purpose which justifies calling it!
     *
     * @param className name of the class
     */
    public OsObjectSchemaInfo(String className) {
        this(nativeCreateRealmObjectSchema(className));
    }

    private OsObjectSchemaInfo(long nativePtr) {
        this.nativePtr = nativePtr;
        NativeContext.dummyContext.addReference(this);
    }

    public String getClassName() {
        return nativeGetClassName(nativePtr);
    }


    public OsObjectSchemaInfo add(String name, RealmFieldType type, boolean primary, boolean indexed, boolean required) {
        final Property property = new Property(name, type, primary, indexed, required);
        try {
            nativeAddProperty(nativePtr, property.getNativePtr());
        } finally {
            property.close();
        }
        return this;
    }

    public OsObjectSchemaInfo add(String name, RealmFieldType type, String linkedClassName) {
        final Property property = new Property(name, type, linkedClassName);
        try {
            nativeAddProperty(nativePtr, property.getNativePtr());
        } finally {
            property.close();
        }
        return this;
    }

    @Override
    public long getNativePtr() {
        return nativePtr;
    }

    @Override
    public long getNativeFinalizerPtr() {
        return nativeFinalizerPtr;
    }

    private static native long nativeCreateRealmObjectSchema(String className);

    private static native long nativeGetFinalizerPtr();

    private static native void nativeAddProperty(long nativePtr, long nativePropertyPtr);

    private static native String nativeGetClassName(long nativePtr);
}
