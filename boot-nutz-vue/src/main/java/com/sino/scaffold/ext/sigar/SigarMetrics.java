package com.sino.scaffold.ext.sigar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author kerbores
 *
 */
@Component
public class SigarMetrics implements PublicMetrics {

	Log logger = Logs.get();

	@Autowired
	ApplicationContext applicationContext;

	@Override
	public Collection<Metric<?>> metrics() {
		List<Metric<?>> metrics = new ArrayList<Metric<?>>();
		try {
			final Sigar sigar = SigarFactory.load(applicationContext);
			// CPU
			metrics.add(new Metric<Number>("cpu.total", sigar.getCpu().getTotal()));
			metrics.add(new Metric<Number>("cpu.idle", sigar.getCpu().getIdle()));
			metrics.add(new Metric<Number>("cpu.irq", sigar.getCpu().getIrq()));
			metrics.add(new Metric<Number>("cpu.nice", sigar.getCpu().getNice()));
			metrics.add(new Metric<Number>("cpu.soft.irq", sigar.getCpu().getSoftIrq()));
			metrics.add(new Metric<Number>("cpu.stolen", sigar.getCpu().getStolen()));
			metrics.add(new Metric<Number>("cpu.sys", sigar.getCpu().getSys()));
			metrics.add(new Metric<Number>("cpu.user", sigar.getCpu().getUser()));
			metrics.add(new Metric<Number>("cpu.wait", sigar.getCpu().getWait()));
			metrics.add(new Metric<Number>("cpu.usage", sigar.getCpuPerc().getCombined() * 100));
			// MEM
			metrics.add(new Metric<Number>("mem.free", sigar.getMem().getFree()));
			metrics.add(new Metric<Number>("mem.actual.free", sigar.getMem().getActualFree()));
			metrics.add(new Metric<Number>("mem.actual.used", sigar.getMem().getActualUsed()));
			metrics.add(new Metric<Number>("mem.free.percent", sigar.getMem().getFreePercent()));
			metrics.add(new Metric<Number>("mem.ram", sigar.getMem().getRam()));
			metrics.add(new Metric<Number>("mem.total", sigar.getMem().getTotal()));
			metrics.add(new Metric<Number>("mem.used", sigar.getMem().getUsed()));
			metrics.add(new Metric<Number>("mem.user.percent", sigar.getMem().getUsedPercent()));
			// NET
			metrics.add(new Metric<Number>("tcp.active.opens", sigar.getTcp().getActiveOpens()));
			metrics.add(new Metric<Number>("tcp.attempt.fails", sigar.getTcp().getAttemptFails()));
			metrics.add(new Metric<Number>("tcp.curre.stab", sigar.getTcp().getCurrEstab()));
			metrics.add(new Metric<Number>("tcp.estab.resets", sigar.getTcp().getEstabResets()));
			metrics.add(new Metric<Number>("tcp.in.errs", sigar.getTcp().getInErrs()));
			metrics.add(new Metric<Number>("tcp.in.segs", sigar.getTcp().getInSegs()));
			metrics.add(new Metric<Number>("tcp.out.rsts", sigar.getTcp().getOutRsts()));
			metrics.add(new Metric<Number>("tcp.out.segs", sigar.getTcp().getOutSegs()));
			metrics.add(new Metric<Number>("tcp.passive.opens", sigar.getTcp().getPassiveOpens()));
			metrics.add(new Metric<Number>("tcp.retrans.segs", sigar.getTcp().getRetransSegs()));
			// SWAP
			metrics.add(new Metric<Number>("swap.free", sigar.getSwap().getFree()));
			metrics.add(new Metric<Number>("swap.page.in", sigar.getSwap().getPageIn()));
			metrics.add(new Metric<Number>("swap.page.out", sigar.getSwap().getPageOut()));
			metrics.add(new Metric<Number>("swap.total", sigar.getSwap().getTotal()));
			metrics.add(new Metric<Number>("swap.used", sigar.getSwap().getUsed()));
			metrics.add(new Metric<Number>("swap.usage", sigar.getSwap().getUsed() * 100 / sigar.getSwap().getTotal()));

			// JVM

			Runtime runtime = Runtime.getRuntime();

			long max = runtime.maxMemory();
			long total = runtime.totalMemory();
			long free = runtime.freeMemory();
			long usable = max - total + free;
			double freePercent = 100 * usable / max;
			double usedPercent = 100 - freePercent;

			metrics.add(new Metric<Number>("jvm.max", max));
			metrics.add(new Metric<Number>("jvm.free", free));
			metrics.add(new Metric<Number>("jvm.total", total));
			metrics.add(new Metric<Number>("jvm.usable", usable));
			metrics.add(new Metric<Number>("jvm.free.percent", freePercent));
			metrics.add(new Metric<Number>("jvm.usage", usedPercent));

			long read = 0;
			long write = 0;
			for (FileSystem fs : sigar.getFileSystemList()) {
				if (fs.getType() == FileSystem.TYPE_LOCAL_DISK) {
					try {
						FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
						read += usage.getDiskReadBytes();
						write += usage.getDiskWriteBytes();
					} catch (SigarException e) {
						e.printStackTrace();
					}
				}
			}

			metrics.add(new Metric<Number>("disk.read", read));
			metrics.add(new Metric<Number>("disk.write", write));
		} catch (SigarException e) {
			e.printStackTrace();
		}
		return metrics;
	}
}
