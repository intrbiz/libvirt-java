package org.libvirt;

import org.libvirt.jna.Libvirt;
import org.libvirt.jna.SizeT;
import org.libvirt.jna.StreamPointer;
import static org.libvirt.Library.libvirt;
import static org.libvirt.ErrorHandler.processError;

public class Stream {

    public static int VIR_STREAM_NONBLOCK = (1 << 0);

    /**
     * the native virStreamPtr.
     */
    private StreamPointer VSP;

    /**
     * The Connect Object that represents the Hypervisor of this Domain
     */
    private Connect virConnect;

    Stream(Connect virConnect, StreamPointer VSP) {
        this.virConnect = virConnect;
        this.VSP = VSP;
    }

    /**
     * Request that the in progress data transfer be cancelled abnormally before
     * the end of the stream has been reached
     *
     * @return <em>ignore</em> (always 0)
     */
    public int abort() throws LibvirtException {
        return processError(libvirt.virStreamAbort(VSP));
    }

    /**
     * Register a callback to be notified when a stream becomes writable, or
     * readable.
     *
     * @see <a
     *      href="http://www.libvirt.org/html/libvirt-libvirt.html#virStreamEventAddCallback">Libvirt
     *      Docs</a>
     * @param events
     *            the events to monitor
     * @param cb
     *            the callback method
     * @return <em>ignore</em> (always 0)
     * @throws LibvirtException
     */
    public int addCallback(int events, Libvirt.VirStreamEventCallback cb) throws LibvirtException {
        return processError(libvirt.virStreamEventAddCallback(VSP, events, cb, null, null));
    }

    @Override
    protected void finalize() throws LibvirtException {
        free();
    }

    /**
     * Indicate that there is no further data is to be transmitted on the
     * stream.
     *
     * @return <em>ignore</em> (always 0)
     * @throws LibvirtException
     */
    public int finish() throws LibvirtException {
        return processError(libvirt.virStreamFinish(VSP));
    }

    /**
     * Decrement the reference count on a stream, releasing the stream object if
     * the reference count has hit zero.
     *
     * @return <em>ignore</em> (always 0)
     * @throws LibvirtException
     */
    public int free() throws LibvirtException {
        int success = 0;
        if (VSP != null) {
            processError(libvirt.virStreamFree(VSP));
            VSP = null;
        }

        return success;
    }

    /**
     * Receives data from the stream into the buffer provided.
     *
     * @param data
     *            buffer to put the data into
     * @return the number of bytes read, -1 on error, -2 if the buffer is empty
     * @throws LibvirtException
     */
    public int receive(byte[] data) throws LibvirtException {
        return processError(libvirt.virStreamRecv(VSP, data, new SizeT(data.length)));
    }

    /**
     * Batch receive method
     *
     * @see <a href="http://www.libvirt.org/html/libvirt-libvirt.html#virStreamRecvAll">virStreamRecvAll</a>
     * @param handler
     *            the callback handler
     * @return <em>ignore</em> (always 0)
     * @throws LibvirtException
     */
    public int receiveAll(Libvirt.VirStreamSinkFunc handler) throws LibvirtException {
        return processError(libvirt.virStreamRecvAll(VSP, handler, null));
    }

    /**
     * Remove an event callback from the stream
     *
     * @see <a href="http://www.libvirt.org/html/libvirt-libvirt.html#virStreamEventRemoveCallback">Libvirt Docs</a>
     * @return <em>ignore</em> (always 0)
     * @throws LibvirtException
     */
    public int removeCallback() throws LibvirtException {
        return processError(libvirt.virStreamEventRemoveCallback(VSP));
    }

    /**
     * Write a series of bytes to the stream.
     *
     * @param data
     *            the data to write
     * @return the number of bytes written, -1 on error, -2 if the buffer is
     *         full
     * @throws LibvirtException
     */
    public int send(String data) throws LibvirtException {
        return processError(libvirt.virStreamSend(VSP, data, new SizeT(data.length())));
    }

    /**
     * Batch send method
     *
     * @see <a
     *      href="http://www.libvirt.org/html/libvirt-libvirt.html#virStreamSendAll">Libvirt
     *      Documentation</a>
     * @param handler
     *            the callback handler
     * @return <em>ignore</em> (always 0)
     * @throws LibvirtException
     */
    public int sendAll(Libvirt.VirStreamSourceFunc handler) throws LibvirtException {
        return processError(libvirt.virStreamSendAll(VSP, handler, null));
    }

    /**
     * Changes the set of events to monitor for a stream.
     *
     * @see <a href="http://www.libvirt.org/html/libvirt-libvirt.html#virStreamEventUpdateCallback">Libvirt Docs</a>
     * @param events
     *            the events to monitor
     * @return <em>ignore</em> (always 0)
     * @throws LibvirtException
     */
    public int updateCallback(int events) throws LibvirtException {
        return processError(libvirt.virStreamEventUpdateCallback(VSP, events));
    }
}
