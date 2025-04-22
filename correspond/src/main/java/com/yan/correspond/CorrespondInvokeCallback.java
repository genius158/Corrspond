package com.yan.correspond;

public interface CorrespondInvokeCallback extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    abstract class Stub extends android.os.Binder implements CorrespondInvokeCallback {
        private static final java.lang.String DESCRIPTOR = "com.yan.correspond.CorrespondInvokeCallback";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.yan.correspond.CorrespondInvokeCallback interface,
         * generating a proxy if needed.
         */
        public static CorrespondInvokeCallback asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin instanceof CorrespondInvokeCallback))) {
                return ((CorrespondInvokeCallback) iin);
            }
            return new CorrespondInvokeCallback.Stub.Proxy(obj);
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
                case TRANSACTION_onCorrespondFeedback: {
                    data.enforceInterface(descriptor);
                    CorrespondInvokeApi _arg0;
                    _arg0 = CorrespondInvokeApi.Stub.asInterface(data.readStrongBinder());
                    this.onCorrespondFeedback(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements CorrespondInvokeCallback {
            private android.os.IBinder mRemote;

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
            public void onCorrespondFeedback(CorrespondInvokeApi invoker) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((invoker != null)) ? (invoker.asBinder()) : (null)));
                    boolean _status = mRemote.transact(CorrespondInvokeCallback.Stub.TRANSACTION_onCorrespondFeedback, _data, _reply, 0);
                    if (!_status) {
                        return;
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_onCorrespondFeedback = android.os.IBinder.FIRST_CALL_TRANSACTION;
    }

    public void onCorrespondFeedback(CorrespondInvokeApi invoker) throws android.os.RemoteException;
}
