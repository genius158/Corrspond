package com.yan.correspond;

import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public interface CorrespondInvokeApi extends android.os.IInterface, CorrespondInvoker {

    /**
     * Local-side IPC implementation stub class.
     */
    abstract class Stub extends android.os.Binder implements CorrespondInvokeApi {
        private static final java.lang.String DESCRIPTOR = "com.yan.correspond.ICorrespondInvoke";
        final static int TYPE_UNKNOWN = -2;
        final static int TYPE_VOID = -1;
        final static int TYPE_BOOLEAN = 0;
        final static int TYPE_INT = 1;
        final static int TYPE_FLOAT = 2;
        final static int TYPE_DOUBLE = 3;
        final static int TYPE_LONG = 4;
        final static int TYPE_STRING = 5;
        final static int TYPE_BYTE = 6;
        final static int TYPE_BOOLEAN_ARRAY = 7;
        final static int TYPE_INT_ARRAY = 8;
        final static int TYPE_FLOAT_ARRAY = 9;
        final static int TYPE_DOUBLE_ARRAY = 10;
        final static int TYPE_LONG_ARRAY = 11;
        final static int TYPE_STRING_ARRAY = 12;
        final static int TYPE_BYTE_ARRAY = 13;
        final static int TYPE_PARCEL = 14;
        final static int TYPE_PARCEL_ARRAY = 15;
        final static int TYPE_PARCEL_LIST = 16;
        final static int TYPE_STRING_LIST = 17;
        final static int TYPE_BINDER = 18;

        private final static Object[] NONE = new Object[]{};

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.yan.correspond.ICorrespondInvoke interface,
         * generating a proxy if needed.
         */
        public static CorrespondInvokeApi asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin instanceof CorrespondInvokeApi))) {
                return ((CorrespondInvokeApi) iin);
            }
            return new CorrespondInvokeApi.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_invoke: {
                    data.enforceInterface(descriptor);
                    java.lang.String _arg0;
                    _arg0 = data.readString();

                    Object[] _argsObj = NONE;
                    if ((0 != data.readInt())) {
                        _argsObj = ParamHelper.readObjArrFromParcel(data);
                    }
                    Object _result = this.invoke(_arg0, _argsObj);
                    reply.writeNoException();
                    ParamHelper.writeObj2Parcel(_result, reply);
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements CorrespondInvokeApi {
            private final android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }


            @Override
            public Object invoke(@NonNull java.lang.String tag, Object... params) {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                Object _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(tag);
                    if (params != null && params.length > 0) {
                        _data.writeInt(1);
                        ParamHelper.writeObjArr2Parcel(params, _data);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status;
                    try {
                        _status = mRemote.transact(Stub.TRANSACTION_invoke, _data, _reply, 0);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    if (!_status) {
                        return null;
                    }
                    _reply.readException();
                    _result = ParamHelper.readObjFromParcel(_reply);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }

        static final int TRANSACTION_invoke = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);

        static class ParamHelper {

            static void writeObj2Parcel(Object obj, Parcel parcel) {
                if (obj == null) {
                    parcel.writeInt(TYPE_VOID);
                } else if (obj instanceof Integer) {
                    parcel.writeInt(TYPE_INT);
                    parcel.writeInt((Integer) obj);
                } else if (obj instanceof Boolean) {
                    parcel.writeInt(TYPE_BOOLEAN);
                    parcel.writeInt(Boolean.compare((boolean) obj, true));
                } else if (obj instanceof Float) {
                    parcel.writeInt(TYPE_FLOAT);
                    parcel.writeFloat((Float) obj);
                } else if (obj instanceof Double) {
                    parcel.writeInt(TYPE_DOUBLE);
                    parcel.writeDouble((Double) obj);
                } else if (obj instanceof Long) {
                    parcel.writeInt(TYPE_LONG);
                    parcel.writeLong((Long) obj);
                } else if (obj instanceof String) {
                    parcel.writeInt(TYPE_STRING);
                    parcel.writeString((String) obj);
                } else if (obj instanceof Byte) {
                    parcel.writeInt(TYPE_BYTE);
                    parcel.writeByte((Byte) obj);
                } else if (obj instanceof int[]) {
                    parcel.writeInt(TYPE_INT_ARRAY);
                    parcel.writeInt(((int[]) obj).length);
                    parcel.writeIntArray((int[]) obj);
                } else if (obj instanceof float[]) {
                    parcel.writeInt(TYPE_FLOAT_ARRAY);
                    parcel.writeInt(((float[]) obj).length);
                    parcel.writeFloatArray((float[]) obj);
                } else if (obj instanceof double[]) {
                    parcel.writeInt(TYPE_DOUBLE_ARRAY);
                    parcel.writeInt(((double[]) obj).length);
                    parcel.writeDoubleArray((double[]) obj);
                } else if (obj instanceof long[]) {
                    parcel.writeInt(TYPE_LONG_ARRAY);
                    parcel.writeInt(((long[]) obj).length);
                    parcel.writeLongArray((long[]) obj);
                } else if (obj instanceof String[]) {
                    parcel.writeInt(TYPE_STRING_ARRAY);
                    parcel.writeInt(((String[]) obj).length);
                    parcel.writeStringArray((String[]) obj);
                } else if (obj instanceof boolean[]) {
                    parcel.writeInt(TYPE_BOOLEAN_ARRAY);
                    parcel.writeInt(((boolean[]) obj).length);
                    parcel.writeBooleanArray((boolean[]) obj);
                } else if (obj instanceof byte[]) {
                    parcel.writeInt(TYPE_BYTE_ARRAY);
                    parcel.writeInt(((byte[]) obj).length);
                    parcel.writeByteArray((byte[]) obj);
                } else if (obj instanceof Parcelable) {
                    parcel.writeInt(TYPE_PARCEL);
                    parcel.writeParcelable((Parcelable) obj, 0);
                } else if (obj instanceof Parcelable[]) {
                    parcel.writeInt(TYPE_PARCEL_ARRAY);
                    parcel.writeParcelableArray((Parcelable[]) obj, 0);
                } else if (obj instanceof IBinder) {
                    parcel.writeInt(TYPE_BINDER);
                    parcel.writeStrongBinder((IBinder) obj);
                } else if (obj instanceof List) {
                    boolean isWrite = false;
                    List list = (List) obj;
                    if (!list.isEmpty()) {
                        Object first = list.get(0);
                        if (first instanceof String) {
                            parcel.writeInt(TYPE_STRING_LIST);
                            parcel.writeStringList(list);
                            isWrite = true;
                        } else if (first instanceof Parcelable) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                parcel.writeInt(TYPE_PARCEL_LIST);
                                parcel.writeParcelableList(list, 0);
                                isWrite = true;
                            }
                        }
                    }
                    if (!isWrite) {
                        Log.e("CorrespondInvokeApi", "TYPE_UNKNOWN : " + obj);
                        parcel.writeInt(TYPE_UNKNOWN);
                    }
                } else {
                    Log.e("CorrespondInvokeApi", "TYPE_UNKNOWN : " + obj);
                    parcel.writeInt(TYPE_UNKNOWN);
                }
            }

            static Object readObjFromParcel(Parcel parcel) {
                int _resType = parcel.readInt();
                Object _result = null;
                int _length;
                switch (_resType) {
                    case TYPE_UNKNOWN: {
                        Log.e("CorrespondInvokeApi", "invoke: return type unknown");
                        break;
                    }
                    case TYPE_VOID: {
                        break;
                    }
                    case TYPE_INT:
                        _result = parcel.readInt();
                        break;
                    case TYPE_BOOLEAN:
                        _result = parcel.readInt() == 0;
                        break;
                    case TYPE_FLOAT:
                        _result = parcel.readFloat();
                        break;
                    case TYPE_DOUBLE:
                        _result = parcel.readDouble();
                        break;
                    case TYPE_LONG:
                        _result = parcel.readLong();
                        break;
                    case TYPE_BYTE:
                        _result = parcel.readByte();
                        break;
                    case TYPE_STRING:
                        _result = parcel.readString();
                        break;
                    case TYPE_BOOLEAN_ARRAY:
                        _length = parcel.readInt();
                        boolean[] _bArr = new boolean[_length];
                        parcel.readBooleanArray(_bArr);
                        _result = _bArr;
                        break;

                    case TYPE_INT_ARRAY:
                        _length = parcel.readInt();
                        int[] _iArr = new int[_length];
                        parcel.readIntArray(_iArr);
                        _result = _iArr;
                        break;
                    case TYPE_FLOAT_ARRAY:
                        _length = parcel.readInt();
                        float[] _fArr = new float[_length];
                        parcel.readFloatArray(_fArr);
                        _result = _fArr;
                        break;

                    case TYPE_DOUBLE_ARRAY:
                        _length = parcel.readInt();
                        double[] _dArr = new double[_length];
                        parcel.readDoubleArray(_dArr);
                        _result = _dArr;
                        break;
                    case TYPE_LONG_ARRAY:
                        _length = parcel.readInt();
                        long[] _lArr = new long[_length];
                        parcel.readLongArray(_lArr);
                        _result = _lArr;
                        break;

                    case TYPE_STRING_ARRAY:
                        _length = parcel.readInt();
                        String[] _sArr = new String[_length];
                        parcel.readStringArray(_sArr);
                        _result = _sArr;
                        break;

                    case TYPE_BYTE_ARRAY:
                        _length = parcel.readInt();
                        byte[] _byteArr = new byte[_length];
                        parcel.readByteArray(_byteArr);
                        _result = _byteArr;
                        break;

                    case TYPE_PARCEL:
                        _result = parcel.readParcelable(CorrespondInvokeApi.class.getClassLoader());
                        break;
                    case TYPE_PARCEL_ARRAY:
                        _result = parcel.readParcelableArray(CorrespondInvokeApi.class.getClassLoader());
                        break;

                    case TYPE_STRING_LIST:
                        List<String> _listString = new ArrayList<>();
                        parcel.readStringList(_listString);
                        _result = _listString;
                        break;

                    case TYPE_PARCEL_LIST:
                        ArrayList<Parcelable> _listParcel = new ArrayList<>();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            parcel.readParcelableList(_listParcel, CorrespondInvokeApi.class.getClassLoader());
                        }
                        _result = _listParcel;
                        break;
                    case TYPE_BINDER:
                        _result = parcel.readStrongBinder();
                        break;
                }
                return _result;
            }

            static void writeObjArr2Parcel(Object[] arr, Parcel parcel) {
                if (arr != null) {
                    parcel.writeInt(arr.length);
                    for (Object param : arr) {
                        writeObj2Parcel(param, parcel);
                    }
                }
            }

            static Object[] readObjArrFromParcel(Parcel parcel) {
                int count = parcel.readInt();
                Object[] params = new Object[count];
                for (int i = 0; i < count; i++) {
                    Object obj = readObjFromParcel(parcel);
                    params[i] = obj;
                }
                return params;
            }
        }
    }


}
