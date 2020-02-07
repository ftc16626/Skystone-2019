package com.ftc16626.missioncontrol.util.profiles

import android.util.Log
import com.google.gson.Gson
import java.io.*

class PilotProfileHandler(val path: File) {
    val profileList = mutableListOf<PilotProfile>()
    var currentProfilePos = 0

    fun init() {
        Log.i("MissionControl", "Looking for profiles...")
        path.walk().forEach {
            if (!it.isDirectory) {
                Log.i("MissionControl", it.name)

                try {
                    val profile = Gson().fromJson(it.readText(), PilotProfile::class.java)

                    profileList.add(profile)
                } catch(e: Exception) {}
            }
        }
    }

    fun incremementPosition() {
        currentProfilePos++
        if (currentProfilePos >= profileList.size)
            currentProfilePos = 0
    }

    fun decrementPosition() {
        currentProfilePos--
        if (currentProfilePos < 0)
            currentProfilePos = profileList.size - 1
    }

    fun getCurrentProfile(): PilotProfile? {
        if (profileList.size == 0) return null
        return profileList[currentProfilePos]
    }

    fun addProfile(profile: PilotProfile) {
        profileList.add(profile)

       writeProfileToFile(profile)
    }

    private fun writeProfileToFile(profile: PilotProfile) {
        val name = profile.name
        File("$path/pilot-$name.json").writeText(profile.toJSON())
    }
}
