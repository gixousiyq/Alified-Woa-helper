package com.vayu.woahelperkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import com.topjohnwu.superuser.ShellUtils
import com.vayu.woahelperkotlin.elements.dialog
import com.vayu.woahelperkotlin.utils.MemoryUtils
import com.vayu.woahelperkotlin.utils.RAM



object functions {
    var ramvalue: Int = 0;
    val isAB = ShellUtils.fastCmd("getprop ro.boot.slot_suffix") != ""
    var mounted = "Mount"
    val storagePath = Environment.getExternalStorageDirectory().path
    var supportsModem = true
    var needsSensor = false
    // false if it doesnt detect and true if it did detect
    var bootImgCheck: Boolean = false
    val busybox = checkBusybox()

    fun checkBusybox(): String {
        val magiskBusyBox = ShellUtils.fastCmd("su -c find /data/adb | grep busybox | grep magisk")
        val ksuBusyBox = ShellUtils.fastCmd("su -c find /data/adb | grep busybox | grep ksu")
        if (magiskBusyBox.isNotEmpty()) {
            return magiskBusyBox
        }
        else if (ksuBusyBox.isNotEmpty()) {
            return ksuBusyBox
        }
        else return ""
    }

    fun checkDevice(): String {
        checkBootImg()
        val readlink = ShellUtils.fastCmd("su -c readlink -f /dev/block/bootdevice/by-name/win")
        val mount = ShellUtils.fastCmd("su -c mount | grep $readlink")
        if (mount == "") {
            mounted = "Mount"
        } else {
            mounted = "Unmount"
        }
        ShellUtils.fastCmd("su -c mkdir $storagePath/UEFI")
        return ShellUtils.fastCmd("getprop ro.build.product")
    }

    fun DeterminePicture(): String {
        val device = checkDevice()
        val vayu = listOf<String>("vayu", "bhima")
        val raphael = listOf<String>("raphael", "raphaelin")

        for (i in vayu) {
            if (device == i) {
                return "vayu"
            }
        }
        if (device == "nabu") {
            supportsModem = false
            return "nabu"
        }
        for (i in raphael) {
            if (device == i) {
                return "raphael"
            }
        }
        // if everything else fails
        return "unknown"
    }

    fun checkRam(context: Context) {
        var stram = MemoryUtils.extractNumberFromString(RAM().getMemory(context))
        var ram = Integer.parseInt(stram);
        if (ram > 600 && ram < 800) {
            ramvalue = 8;
        } else if (ram > 800 && ram < 1200) {
            ramvalue = 12;
        } else {
            ramvalue = 6
        }
    }

    fun checkPanel(): String {
        // command taken from marius586 tanks to him
        var panelCmd =
            ShellUtils.fastCmd("echo $(su -c cat /proc/cmdline | tr \" :=\" \"\n\" | grep dsi) | tr \" \" \"\n\" | tail -1")
        val hauxing =
            listOf<String>("dsi_j20s_42_02_0b_video_display", "dsi_k82_42_02_0a_dual_cphy_video")
        val tianma =
            listOf<String>("dsi_j20s_36_02_0a_video_display", "dsi_k82_36_02_0b_dual_cphy_video")
        for (i in hauxing) {
            if (i == panelCmd) {
                return "Hauxing"
            }
        }
        for (i in tianma) {
            if (i == panelCmd) {
                return "Tianma"
            }
        }
        var list = panelCmd.split("_")
        return list[list.size - 1]
    }

    fun getLink(guideOrgroup: String): String {
        var vayu = listOf<String>(
            "https://github.com/woa-vayu/Port-Windows-11-POCO-X3-Pro", // guide
            "https://www.t.me/winonvayualt"
        ) // Group
        var nabu = listOf<String>(
            "https://github.com/erdilS/Port-Windows-11-Xiaomi-Pad-5",
            "https://t.me/nabuwoa"
        )
        var raphael = listOf<String>(
            "https://github.com/graphiks/woa-raphael",
            "https://t.me/woaraphael"
        )
        when {
            guideOrgroup == "guide" -> {
                if (DeterminePicture() == "vayu") return vayu[0]
                if (DeterminePicture() == "nabu") return nabu[0]
                if (DeterminePicture() == "raphael") return raphael[0]
            }

            guideOrgroup == "group" -> {
                if (DeterminePicture() == "vayu") return vayu[1]
                if (DeterminePicture() == "nabu") return nabu[1]
                if (DeterminePicture() == "raphael") return raphael[1]
            }
        }
        return ""
    }


    fun backupBoot(destination: String, updateDialogAfterFinish: Boolean, checkBootImg: Boolean = true) {
        if (destination == "android") {
            ShellUtils.fastCmd("su -c dd " +
                "if=/dev/block/bootdevice/by-name/boot\$(getprop ro.boot.slot_suffix) " +
                "of=$storagePath/boot.img bs=16M")
            if (updateDialogAfterFinish) updateDialogAfterFinishingTheTask()
        }
        if (destination == "windows") {
            if (mounted == "Mount") {
                mount(false)
                ShellUtils.fastCmd("su -c dd " +
                        "if=/dev/block/bootdevice/by-name/boot\$(getprop ro.boot.slot_suffix) " +
                        "of=$storagePath/Windows/boot.img bs=16M")
                unmount(false)
                if (checkBootImg) checkBootImg()
            }
            if (mounted == "Unmount") {
                ShellUtils.fastCmd("su -c dd " +
                        "if=/dev/block/bootdevice/by-name/boot\$(getprop ro.boot.slot_suffix) " +
                        "of=$storagePath/Windows/boot.img bs=16M")
                if (checkBootImg) checkBootImg()
            }
            if (updateDialogAfterFinish) updateDialogAfterFinishingTheTask()
        }
    }

    fun mount(updateDialogAfterFinish: Boolean) {
        ShellUtils.fastCmd("su -c mkdir $storagePath/Windows")
        ShellUtils.fastCmd("su -mm -c busybox mount -t ntfs -o allow_other" +
                " /dev/block/by-name/win $storagePath/Windows")
        mounted = "Unmount"
        if (updateDialogAfterFinish) {
            updateDialogAfterFinishingTheTask()
        }
    }

    fun unmount(updateDialogAfterFinish: Boolean) {
        ShellUtils.fastCmd("su -mm -c umount $storagePath/Windows")
        ShellUtils.fastCmd("su -c rmdir $storagePath/Windows")
        mounted = "Mount"
        if (updateDialogAfterFinish) {
            updateDialogAfterFinishingTheTask()
        }
    }

    fun dump_modem(updateDialogAfterFinish: Boolean) {
        when {
            mounted == "Mount" -> {
                mount(false)
                // code kanged from autoprivosin zip made by birabub or idk who actualy made it
                val path = ShellUtils.fastCmd("find $storagePath/Windows/Windows/System32/DriverStore/FileRepository -name \"qcremotefs8150.inf_arm64_*\"")
                ShellUtils.fastCmd("dd if=/dev/block/by-name/modemst1 of=$path/bootmodem_fs1 bs=8388608")
                ShellUtils.fastCmd("dd if=/dev/block/by-name/modemst2 of=$path/bootmodem_fs2 bs=8388608")
                unmount(false)
            }
            mounted == "Unmount" -> {
                val path = ShellUtils.fastCmd("find $storagePath/Windows/Windows/System32/DriverStore/FileRepository -name \"qcremotefs8150.inf_arm64_*\"")
                ShellUtils.fastCmd("dd if=/dev/block/by-name/modemst1 of=$path/bootmodem_fs1 bs=8388608")
                ShellUtils.fastCmd("dd if=/dev/block/by-name/modemst2 of=$path/bootmodem_fs2 bs=8388608")
            }
        }
        if (updateDialogAfterFinish) {
            updateDialogAfterFinishingTheTask()
        }
    }

    fun updateDialogAfterFinishingTheTask() {
        dialog.proceed_to_loading.value = false
        dialog.is_finished = true
    }

    fun flash(updateDialogAfterFinish: Boolean) {
        val uefi_name = ShellUtils.fastCmd("su -c ls $storagePath/UEFI | grep .img")
        // Log.println(Log.DEBUG, "Main", c.getString(R.string.cmd_flash_uefi, storagePath ,uefi_name))
        ShellUtils.fastCmd("su -c dd if=$storagePath/UEFI/$uefi_name of=/dev/block/bootdevice/by-name/boot\$(getprop ro.boot.slot_suffix) bs=16M")
        if (updateDialogAfterFinish) {
            updateDialogAfterFinishingTheTask()
        }
    }

    fun quickboot(context: Context) {
        mount(false)
        flash(false)
        // prefrence stuff
        val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (sharedPrefs.getBoolean("force_backup_check", false)) {
            if (sharedPrefs.getBoolean("force_backup_android", false)) {
                backupBoot("android", false, false)
            }
            if (sharedPrefs.getBoolean("force_backup_windows", false)) {
                backupBoot("windows", false, false)
            }
        }
        if (sharedPrefs.getBoolean("detected_backup_check", false)) {
            var ThereIsABootImgInWindows = bootImgCheck
            if (!ThereIsABootImgInWindows) {
                if (sharedPrefs.getBoolean("detected_backup_android", false)) {
                    backupBoot("android", false, false)
                }
                if (sharedPrefs.getBoolean("detected_backup_windows", false)) {
                    backupBoot("windows", false, false)
                }
            }
        }
        if (supportsModem) {
            dump_modem(false)
        }
        // command from m3k app
        ShellUtils.fastCmd("su -c svc power reboot")
        updateDialogAfterFinishingTheTask()
    }

    fun onDismiss() {
        if (dialog.proceed_to_loading.value == true) {
        } else {
            dialog.is_finished = false
            dialog.show_backup_dialog.value = false
            dialog.show_mount_dialog.value = false
            dialog.show_modem_dialog.value = false
            dialog.show_uefi_dialog.value = false
            dialog.show_quickboot_dialog.value = false
        }
    }


    // Will return false if it doesnt detect and true if it did detect
    fun checkBootImg() {
        var aRandomCheck = false
        if (mounted == "Mount") {
            mount(false)
            aRandomCheck = true
        }
        var check = ShellUtils.fastCmd("su -c ls ${functions.storagePath}/Windows/boot.img")
        bootImgCheck = if (check == "") false else true
        if (aRandomCheck) unmount(false)
    }

    // Will return "Multiple Images Detected Error" or "Everything is good" or "" if it didnt detect any
    fun checkUEFI(): String {
        var imagesCount = ShellUtils.fastCmd("su -c ls $storagePath/UEFI | grep -c .img").toInt()
        when {
            imagesCount > 1 -> return "Multiple Images Detected Error"
            imagesCount == 1 -> return "Everything is good"
            imagesCount < 1 -> return ""
            else -> return ""
        }
    }
}