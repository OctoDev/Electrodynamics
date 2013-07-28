package electrodynamics.client.fx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import electrodynamics.util.EDVector3;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class FXLightningBoltCommon {
	ArrayList<Segment> segments;
	EDVector3 start;
	EDVector3 end;
	HashMap<Integer, Integer> splitparents;
	public float multiplier;
	public float length;
	public int numsegments0;
	public int increment;
	public int type = 0;
	public boolean nonLethal = false;
	private int numsplits;
	private boolean finalized;
	private Random rand;
	public long seed;
	public int particleAge;
	public int particleMaxAge;
	private AxisAlignedBB boundingBox;
	public Entity wrapper;
	public static final float speed = 3.0F;
	public static final int fadetime = 20;
	public int damage;

	public FXLightningBoltCommon(World world, EDVector3 jammervec, EDVector3 targetvec, long seed) {
		this.segments = new ArrayList<Segment>();
		this.splitparents = new HashMap<Integer, Integer>();
		this.start = jammervec;
		this.end = targetvec;
		this.seed = seed;
		this.rand = new Random(seed);
		this.numsegments0 = 1;
		this.increment = 1;
		this.length = this.end.copy().sub(this.start).length();
		this.particleMaxAge = 30;
		this.multiplier = 1.0F;
		this.particleAge = (-(int) (this.length * 3.0F));
		this.boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.boundingBox.setBB(AxisAlignedBB.getBoundingBox(Math.min(this.start.x, this.end.x), Math.min(this.start.y, this.end.y), Math.min(this.start.z, this.end.z), Math.max(this.start.x, this.end.x), Math.max(this.start.y, this.end.y), Math.max(this.start.z, this.end.z)).expand(this.length / 2.0F, this.length / 2.0F, this.length / 2.0F));

		this.segments.add(new Segment(this.start, this.end));
	}

	public FXLightningBoltCommon(World world, Entity detonator, Entity target, long seed) {
		this(world, new EDVector3(detonator), new EDVector3(target), seed);
	}

	public FXLightningBoltCommon(World world, Entity detonator, Entity target, long seed, int speed) {
		this(world, new EDVector3(detonator), new EDVector3(target.posX, target.posY + target.getEyeHeight() - 0.699999988079071D, target.posZ), seed);
		this.increment = speed;
		this.multiplier = 0.4F;
	}

	public FXLightningBoltCommon(World world, TileEntity detonator, Entity target, long seed) {
		this(world, new EDVector3(detonator), new EDVector3(target), seed);
	}

	public FXLightningBoltCommon(World world, TileEntity detonator, double x, double y, double z, long seed) {
		this(world, new EDVector3(detonator), new EDVector3(x, y, z), seed);
	}

	public FXLightningBoltCommon(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi) {
		this(world, new EDVector3(x1, y1, z1), new EDVector3(x, y, z), seed);
		this.particleMaxAge = (duration + this.rand.nextInt(duration) - duration / 2);
		this.multiplier = multi;
	}

	public FXLightningBoltCommon(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed) {
		this(world, new EDVector3(x1, y1, z1), new EDVector3(x, y, z), seed);
		this.particleMaxAge = (duration + this.rand.nextInt(duration) - duration / 2);
		this.multiplier = multi;
		this.increment = speed;
	}

	public void setWrapper(Entity entity) {
		this.wrapper = entity;
	}

	public void setMultiplier(float m) {
		this.multiplier = m;
	}

	public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle) {
		if (this.finalized) return;
		
		ArrayList<Segment> oldsegments = this.segments;
		this.segments = new ArrayList<Segment>();
		Segment prev = null;
		for (Iterator<Segment> iterator = oldsegments.iterator(); iterator.hasNext();) {
			Segment segment = iterator.next();
			prev = segment.prev;
			EDVector3 subsegment = segment.diff.copy().scale(1.0F / splits);
			BoltPoint[] newpoints = new BoltPoint[splits + 1];
			EDVector3 startpoint = segment.startpoint.point;
			newpoints[0] = segment.startpoint;
			newpoints[splits] = segment.endpoint;
			for (int i = 1; i < splits; i++) {
				EDVector3 randoff = EDVector3.getPerpendicular(segment.diff).rotate(this.rand.nextFloat() * 360.0F, segment.diff);
				randoff.scale((this.rand.nextFloat() - 0.5F) * amount);
				EDVector3 basepoint = startpoint.copy().add(subsegment.copy().scale(i));
				newpoints[i] = new BoltPoint(basepoint, randoff);
			}

			for (int i = 0; i < splits; i++) {
				Segment next = new Segment(newpoints[i], newpoints[(i + 1)], segment.light, segment.segmentno * splits + i, segment.splitno);
				next.prev = prev;
				if (prev != null) prev.next = next;
				
				if ((i != 0) && (this.rand.nextFloat() < splitchance)) {
					EDVector3 splitrot = EDVector3.xCrossProduct(next.diff).rotate(this.rand.nextFloat() * 360.0F, next.diff);
					EDVector3 diff = next.diff.copy().rotate((this.rand.nextFloat() * 0.66F + 0.33F) * splitangle, splitrot).scale(splitlength);
					this.numsplits += 1;
					this.splitparents.put(Integer.valueOf(this.numsplits), Integer.valueOf(next.splitno));
					Segment split = new Segment(newpoints[i], new BoltPoint(newpoints[(i + 1)].basepoint, newpoints[(i + 1)].offsetvec.copy().add(diff)), segment.light / 2.0F, next.segmentno, this.numsplits);
					split.prev = prev;
					this.segments.add(split);
				}
				prev = next;
				this.segments.add(next);
			}

			if (segment.next != null) {
				segment.next.prev = prev;
			}
		}
		this.numsegments0 *= splits;
	}

	public void defaultFractal() {
		fractal(2, this.length * this.multiplier / 8.0F, 0.7F, 0.1F, 45.0F);
		fractal(2, this.length * this.multiplier / 12.0F, 0.5F, 0.1F, 50.0F);
		fractal(2, this.length * this.multiplier / 17.0F, 0.5F, 0.1F, 55.0F);
		fractal(2, this.length * this.multiplier / 23.0F, 0.5F, 0.1F, 60.0F);
		fractal(2, this.length * this.multiplier / 30.0F, 0.0F, 0.0F, 0.0F);
		fractal(2, this.length * this.multiplier / 34.0F, 0.0F, 0.0F, 0.0F);
	}

	private void calculateCollisionAndDiffs() {
		HashMap<Integer, Integer> lastactivesegment = new HashMap<Integer, Integer>();
		Collections.sort(this.segments, new SegmentSorter());
		int lastsplitcalc = 0;
		int lastactiveseg = 0;
		float splitresistance = 0.0F;
		for (Iterator<Segment> iterator = this.segments.iterator(); iterator.hasNext();) {
			Segment segment = iterator.next();
			if (segment.splitno > lastsplitcalc) {
				lastactivesegment.put(Integer.valueOf(lastsplitcalc), Integer.valueOf(lastactiveseg));
				lastsplitcalc = segment.splitno;
				lastactiveseg = lastactivesegment.get(this.splitparents.get(Integer.valueOf(segment.splitno))).intValue();
				splitresistance = lastactiveseg >= segment.segmentno ? 0.0F : 50.0F;
			}
			if (splitresistance < 40.0F * segment.light) {
				lastactiveseg = segment.segmentno;
			}
		}

		lastactivesegment.put(Integer.valueOf(lastsplitcalc), Integer.valueOf(lastactiveseg));
		lastsplitcalc = 0;
		lastactiveseg = lastactivesegment.get(Integer.valueOf(0)).intValue();
		Segment segment;
		for (Iterator<Segment> iterator = this.segments.iterator(); iterator.hasNext(); segment.calcEndDiffs()) {
			segment = iterator.next();
			if (lastsplitcalc != segment.splitno) {
				lastsplitcalc = segment.splitno;
				lastactiveseg = lastactivesegment.get(Integer.valueOf(segment.splitno)).intValue();
			}
			if (segment.segmentno > lastactiveseg) {
				iterator.remove();
			}
		}
	}

	public void finalizeBolt() {
		if (this.finalized) {
			return;
		}

		this.finalized = true;
		calculateCollisionAndDiffs();
		Collections.sort(this.segments, new SegmentLightSorter());
	}

	public void onUpdate() {
		this.particleAge += this.increment;
		if (this.particleAge > this.particleMaxAge)
			this.particleAge = this.particleMaxAge;
	}

	public class BoltPoint {
		EDVector3 point;
		EDVector3 basepoint;
		EDVector3 offsetvec;

		public BoltPoint(EDVector3 basepoint, EDVector3 offsetvec) {
			this.point = basepoint.copy().add(offsetvec);
			this.basepoint = basepoint;
			this.offsetvec = offsetvec;
		}
	}

	public class Segment {
		public FXLightningBoltCommon.BoltPoint startpoint;
		public FXLightningBoltCommon.BoltPoint endpoint;
		public EDVector3 diff;
		public Segment prev;
		public Segment next;
		public EDVector3 nextdiff;
		public EDVector3 prevdiff;
		public float sinprev;
		public float sinnext;
		public float light;
		public int segmentno;
		public int splitno;

		public void calcDiff() {
			this.diff = this.endpoint.point.copy().sub(this.startpoint.point);
		}

		public void calcEndDiffs() {
			if (this.prev != null) {
				EDVector3 prevdiffnorm = this.prev.diff.copy().normalize();
				EDVector3 thisdiffnorm = this.diff.copy().normalize();
				this.prevdiff = thisdiffnorm.add(prevdiffnorm).normalize();
				this.sinprev = ((float) Math.sin(EDVector3.anglePreNorm(thisdiffnorm, prevdiffnorm.scale(-1.0F)) / 2.0F));
			} else {
				this.prevdiff = this.diff.copy().normalize();
				this.sinprev = 1.0F;
			}
			if (this.next != null) {
				EDVector3 nextdiffnorm = this.next.diff.copy().normalize();
				EDVector3 thisdiffnorm = this.diff.copy().normalize();
				this.nextdiff = thisdiffnorm.add(nextdiffnorm).normalize();
				this.sinnext = ((float) Math.sin(EDVector3.anglePreNorm(thisdiffnorm, nextdiffnorm.scale(-1.0F)) / 2.0F));
			} else {
				this.nextdiff = this.diff.copy().normalize();
				this.sinnext = 1.0F;
			}
		}

		@Override
		public String toString() {
			return this.startpoint.point.toString() + " " + this.endpoint.point.toString();
		}

		public Segment(FXLightningBoltCommon.BoltPoint start, FXLightningBoltCommon.BoltPoint end, float light, int segmentnumber, int splitnumber) {
			this.startpoint = start;
			this.endpoint = end;
			this.light = light;
			this.segmentno = segmentnumber;
			this.splitno = splitnumber;
			calcDiff();
		}

		public Segment(EDVector3 start, EDVector3 end) {
			this(new FXLightningBoltCommon.BoltPoint(start, new EDVector3(0.0D, 0.0D, 0.0D)), new FXLightningBoltCommon.BoltPoint(end, new EDVector3(0.0D, 0.0D, 0.0D)), 1.0F, 0, 0);
		}
	}

	public class SegmentLightSorter implements Comparator<Segment> {
		final FXLightningBoltCommon this$0;

		@Override
		public int compare(FXLightningBoltCommon.Segment o1, FXLightningBoltCommon.Segment o2) {
			return Float.compare(o2.light, o1.light);
		}

		public SegmentLightSorter() {
			this.this$0 = FXLightningBoltCommon.this;
		}
	}

	public class SegmentSorter implements Comparator<Segment> {
		final FXLightningBoltCommon this$0;

		@Override
		public int compare(FXLightningBoltCommon.Segment o1, FXLightningBoltCommon.Segment o2) {
			int comp = Integer.valueOf(o1.splitno).compareTo(Integer.valueOf(o2.splitno));
			if (comp == 0) {
				return Integer.valueOf(o1.segmentno).compareTo(Integer.valueOf(o2.segmentno));
			}
			return comp;
		}

		public SegmentSorter() {
			this.this$0 = FXLightningBoltCommon.this;
		}
	}
}