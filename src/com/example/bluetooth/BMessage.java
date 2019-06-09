package com.example.bluetooth;

class BMessage
{
    byte[] bdata;
    String data;
    int id;
    int len;
    String type;
    
    BMessage(final String type, final byte[] array, final int len, final int id) {
        if (array != null && array.length != 0) {
            this.bdata = new byte[array.length];
        }
        this.type = type;
        this.len = len;
        this.id = id;
        if (array != null) {
            this.bdata = array.clone();
        }
    }
}
