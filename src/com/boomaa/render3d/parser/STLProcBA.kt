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

    // Get 32-bit (4 byte) unsigned int in little endian
    fun getNextUInt32(): Int {
        return nextFourByteBuffer().int
    }

    // Get 32-bit (4 byte) float in little endian
    fun getNextFloat32(): Float {
        return nextFourByteBuffer().float
    }

    private fun nextFourByteBuffer(byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): ByteBuffer {
        return ByteBuffer.wrap(this.subList(0, 4).toByteArray()).order(byteOrder)
    }
}