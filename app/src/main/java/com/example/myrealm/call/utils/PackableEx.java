package com.example.myrealm.call.utils;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
