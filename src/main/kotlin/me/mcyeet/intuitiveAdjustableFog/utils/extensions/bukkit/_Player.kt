package me.mcyeet.intuitiveAdjustableFog.utils.extensions.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import me.mcyeet.intuitiveAdjustableFog.IntuitiveAdjustableFog.Companion.Plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object _Player {

    fun Player.sendPackets(packets: Collection<PacketWrapper<*>>) {
        packets.forEach { this.sendPacket(it) }
    }

    fun Player.sendPacket(packet: PacketWrapper<*>) {
        Bukkit.getScheduler().runTaskAsynchronously(Plugin, Runnable {
            PacketEvents.getAPI().playerManager.sendPacket(this, packet)
        })
    }

}
