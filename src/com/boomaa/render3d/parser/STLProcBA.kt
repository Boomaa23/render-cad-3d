package com.boomaa.render3d.parser

import java.nio.ByteBuffer
import java.nio.ByteOrder

class STLProcBA : ArrayList<Byte>() {
    private var counter = 0

    override fun get(index: Int): Byte {
        counter++
        return super.get(index + (counter - 1))
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Byte> {
        val len = toIndex - fromIndex
        counter += len
        return super.subList(fromIndex + counter - len, toIndex + counter - len)
    }

    fun skipBytes(n: Int) {
        counter += n
    }

    // Get unsigned 32-bit (4 byte) int in little endian
    fun getNextUInt32(): Int {
        return ByteBuffer.wrap(nextFourBytes()).order(ByteOrder.LITTLE_ENDIAN).int
    }

    // Get 32-bit (4 byte) float in little endian
    fun getNextFloat32(): Float {
        return ByteBuffer.wrap(nextFourBytes()).order(ByteOrder.LITTLE_ENDIAN).float
    }

    private fun nextFourBytes(): ByteArray {
        return this.subList(0, 4).toByteArray()
    }
}