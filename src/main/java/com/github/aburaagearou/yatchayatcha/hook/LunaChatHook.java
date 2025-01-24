package com.github.aburaagearou.yatchayatcha.hook;

import com.github.aburaagearou.yatchayatcha.YYConfigUtil;
import com.github.ucchyocean.lc.LunaChat;
import com.github.ucchyocean.lc.LunaChatAPI;
import com.github.ucchyocean.lc.channel.Channel;

/**
 * LunaChat連携クラス
 * @author AburaAgeTarou
 */
public class LunaChatHook {

	/**
	 * LunaChatのチャンネルにメッセージを送信
	 * @param msg メッセージ
	 */
	public static boolean sendToLunaChatChannel(String msg) {
		LunaChatAPI api = LunaChat.getInstance().getLunaChatAPI();
		Channel channel = api.getChannel(YYConfigUtil.getLunaChatAuctionChannel());
		if(channel == null) return false;
		channel.chatFromOtherSource("&e運オク&8", "&7Sys", msg);
		return true;
	}
}
