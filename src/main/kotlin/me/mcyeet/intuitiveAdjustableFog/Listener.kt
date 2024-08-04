package me.mcyeet.intuitiveAdjustableFog

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateSimulationDistance
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateViewDistance
import me.mcyeet.intuitiveAdjustableFog.IntuitiveAdjustableFog.Companion.Config
import me.mcyeet.intuitiveAdjustableFog.IntuitiveAdjustableFog.Companion.Plugin
import me.mcyeet.intuitiveAdjustableFog.utils.extensions.bukkit._OfflinePlayer.hasPerm
import me.mcyeet.intuitiveAdjustableFog.utils.extensions.bukkit._Player.sendPacket
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent

class Listener: Listener, PacketListenerAbstract() {

    @EventHandler
    fun onWorldChange(event: PlayerChangedWorldEvent) {
        if (event.player.hasPerm("intuitiveadjustablefog.ignoreme"))
            return

        val viewDistance = Config.get<Int>("View_Distance")
        val viewDistancePacket = WrapperPlayServerUpdateViewDistance(viewDistance)
        val simulationDistancePacket = WrapperPlayServerUpdateSimulationDistance(viewDistance)

        event.player.sendPacket(viewDistancePacket)
        event.player.sendPacket(simulationDistancePacket)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskLater(Plugin, Runnable {
            if (event.player.hasPerm("intuitiveadjustablefog.ignoreme"))
                return@Runnable

            val viewDistance = Config.get<Int>("View_Distance")
            val viewDistancePacket = WrapperPlayServerUpdateViewDistance(viewDistance)
            val simulationDistancePacket = WrapperPlayServerUpdateSimulationDistance(viewDistance)

            event.player.sendPacket(viewDistancePacket)
            event.player.sendPacket(simulationDistancePacket)
        }, 1L)
    }

    override fun onPacketSend(event: PacketSendEvent) {
        if (event.packetType != PacketType.Play.Server.JOIN_GAME)
            return

        val player = Bukkit.getOfflinePlayer(event.user.uuid)
        if (player.hasPerm("intuitiveadjustablefog.ignoreme"))
            return

        val viewDistance = Config.get<Int>("View_Distance")

        when (event.packetType) {
            PacketType.Play.Server.JOIN_GAME -> {
                val packet = WrapperPlayServerJoinGame(event)
                packet.simulationDistance = viewDistance
                packet.viewDistance = viewDistance
            }

            PacketType.Play.Server.UPDATE_VIEW_DISTANCE -> {
                println("\nPANIC! PANIC! PANIC! PANIC! PANIC! PANIC! PANIC!")
                val packet = WrapperPlayServerUpdateViewDistance(event)
                packet.viewDistance = viewDistance
            }

            PacketType.Play.Server.UPDATE_SIMULATION_DISTANCE -> {
                println("\nPANIC! PANIC! PANIC! PANIC! PANIC! PANIC! PANIC!")
                val packet = WrapperPlayServerUpdateSimulationDistance(event)
                packet.simulationDistance = viewDistance
            }
        }
    }

}