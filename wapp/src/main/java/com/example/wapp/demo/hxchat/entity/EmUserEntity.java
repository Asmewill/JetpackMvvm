package com.example.wapp.demo.hxchat.entity;

import androidx.annotation.NonNull;


import com.hyphenate.easeui.domain.EaseUser;

import java.util.ArrayList;
import java.util.List;


public class EmUserEntity extends EaseUser {
    public EmUserEntity() {
        super();
    }

    public EmUserEntity(@NonNull String username) {
        super(username);
    }

    public static List<EmUserEntity> parseList(List<EaseUser> users) {
        List<EmUserEntity> entities = new ArrayList<>();
        if(users == null || users.isEmpty()) {
            return entities;
        }
        EmUserEntity entity;
        for (EaseUser user : users) {
            entity = parseParent(user);
            entities.add(entity);
        }
        return entities;
    }
    public static EmUserEntity parseParent(EaseUser user) {
        EmUserEntity entity = new EmUserEntity();
        entity.setUsername(user.getUsername());
        entity.setNickname(user.getNickname());
        entity.setAvatar(user.getAvatar());
        entity.setInitialLetter(user.getInitialLetter());
        entity.setContact(user.getContact());
        entity.setEmail(user.getEmail());
        entity.setGender(user.getGender());
        entity.setBirth(user.getBirth());
        entity.setPhone(user.getPhone());
        entity.setSign(user.getSign());
        entity.setExt(user.getExt());
        return entity;
    }
}
