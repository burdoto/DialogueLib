package de.kaleidox.dialib.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class DialogueMenu extends AbstractContainerMenu {
    protected DialogueMenu(@Nullable MenuType<?> p_38851_, int p_38852_) {
        super(p_38851_, p_38852_);
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return false;
    }
}
