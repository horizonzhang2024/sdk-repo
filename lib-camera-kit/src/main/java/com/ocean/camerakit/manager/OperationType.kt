package com.ocean.camerakit.manager

enum class OperationType {
    START_TASK,      // bl_info_task_start
    FINISH_TASK,      // bl_info_task_finish
    CAPTURE,         // bl_info:capture
    START_RECORD,    // bl_info:record_start
    PAUSE_RECORD,    // bl_info:record_pause
    RESUME_RECORD,   // bl_info:record_resume
    STOP_RECORD,      // bl_info:stop
    NORMAL_INFO
}