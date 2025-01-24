package com.github.aburaagearou.yatchayatcha;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import com.github.aburaagearou.yatchayatcha.commands.AdminAuctionCommand;
import com.github.aburaagearou.yatchayatcha.commands.BidCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * YatchaYatcha メインクラス
 * @author AburaAgeTarou
 */
public final class YatchaYatcha extends JavaPlugin {

    // プラグインインスタンス
    private static YatchaYatcha instance;

    // コマンドAPI
    private static PaperCommandManager manager;

    // コマンドリスト
    private static final List<BaseCommand> commands = new ArrayList<>();

    // EconomyAPI
    private static Economy economy;

    /**
     * プラグイン有効化処理
     */
    @Override
    public void onEnable() {
        // プラグインインスタンスをセット
        instance = this;

        getLogger().info("YatchaYatchaを有効化します。");

        // EconomyAPIの取得
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new NullPointerException("EconomyAPIが見つかりません。");
        }
        economy = rsp.getProvider();

        // コマンドAPIの初期化
        manager = new PaperCommandManager(this);

        // brigadierを有効化しろと言われたので有効化
        manager.enableUnstableAPI("brigadier");

        // helpを有効化
        manager.enableUnstableAPI("help");

        // コマンドの登録
        manager.registerCommand(new AdminAuctionCommand().setExceptionHandler((command, registeredCommand, sender, args, t) -> {
            sender.sendMessage(MessageType.ERROR, MessageKeys.ERROR_GENERIC_LOGGED);
            return true;
        }), true);
        manager.registerCommand(new BidCommand().setExceptionHandler((command, registeredCommand, sender, args, t) -> {
            sender.sendMessage(MessageType.ERROR, MessageKeys.ERROR_GENERIC_LOGGED);
            return true;
        }), true);

        getLogger().info("YatchaYatchaを有効化しました。");
    }

    /**
     * プラグイン無効化処理
     */
    @Override
    public void onDisable() {
        getLogger().info("YatchaYatchaを無効化します。");

        // コマンドの登録解除
        commands.forEach(manager::unregisterCommand);
        manager.unregisterCommands();

        getLogger().info("YatchaYatchaを無効化しました。");
    }

    /**
     * プラグインインスタンスを取得する
     * @return プラグインインスタンス
     */
    public static YatchaYatcha getInstance() {
        return instance;
    }

    /**
     * コマンドリストにコマンドを登録する
     * @param command 登録するコマンド
     */
    public static void addCommand(BaseCommand command) {
        commands.add(command);
    }

    /**
     * EconomyAPIを取得する
     * @return Economy APIクラス
     */
    public static Economy getEconomy() {
        return economy;
    }
}
