package com.kacyber.message;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import io.realm.RealmObject;

@SuppressWarnings({"rawtypes","unchecked"})
public enum MessageType {
	/**
	 * 文本
	 */
	TEXT(TextMessageBody.class),
	/**
	 * 图片
	 */
	IMAGE(ImageMessageBody.class),
	/**
	 * 声音
	 */
	AUDIO(AudioMessageBody.class),
	/**
	 * 表情消息
	 */
	EMOJI(EmojiMessageBody.class),
	/**
	 * 地理位置
	 */
	LOCATION(LocationMessageBody.class),
	/**
	 * 文档
	 */
	DOC(DocMessageBody.class),
	/**
	 * 文件
	 */
	FILE(FileMessageBody.class),
    
	/**
	 * 视频
	 */
	VIDEO(VideoMessageBody.class),
	/**
	 * 新闻
	 */
	NEWS(NewsMessageBody.class),
	/**
	 * 音乐
	 */
	MUSIC(MusicMessageBody.class),

	/**
	 * 阅读回执确认
	 */
	COMFIRM(ConfirmMessageBody.class);

	
	private final   Class  bodyClass;

	private MessageType(Class<?> claz) {
		this.bodyClass = claz;
	}
 
	public <T extends MessageBody> Class<T> getBodyClass() {
		return bodyClass;
	}
	
	
	public static void main(String args[]){
		List<String> names=new LinkedList<String>();
		for(MessageType item:MessageType.values()){
			names.add(item.name());
		}
		Log.e("MessageType", "" + names + "','");
	}

}
