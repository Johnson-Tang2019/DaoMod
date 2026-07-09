package com.abyssredemption.daomod.client.model;

import com.abyssredemption.daomod.AbsDaoMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class DaoBeastModel<T extends LivingEntity> extends EntityModel<T> {
    public static ModelLayerLocation layer(int variant) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AbsDaoMod.MODID,
                "dao_beast_" + variant), "main");
    }

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart rearLeftLeg;
    private final ModelPart rearRightLeg;
    private final ModelPart tail;
    private final int variant;

    public DaoBeastModel(ModelPart root, int variant) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.frontLeftLeg = root.getChild("front_left_leg");
        this.frontRightLeg = root.getChild("front_right_leg");
        this.rearLeftLeg = root.getChild("rear_left_leg");
        this.rearRightLeg = root.getChild("rear_right_leg");
        this.tail = root.getChild("tail");
        this.variant = variant;
    }

    public static LayerDefinition createBodyLayer(int variant) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        switch (variant) {
            case 2 -> buildKui(root);
            case 4, 17, 28 -> buildSerpent(root, variant);
            case 5 -> buildMantis(root);
            case 6, 11, 19, 25, 26 -> buildFlyer(root, variant);
            case 9 -> buildSpider(root);
            case 12, 14 -> buildTurtle(root, variant == 14);
            case 20 -> buildRabbit(root);
            case 24 -> buildApe(root);
            case 27 -> buildWarGolem(root);
            default -> buildQuadruped(root, variant);
        }
        return LayerDefinition.create(mesh, 64, 64);
    }

    private static void buildQuadruped(PartDefinition root, int variant) {
        float width = 7 + variant % 5;
        float length = 11 + variant % 7;
        float height = 6 + variant % 4;
        add(root, "body", CubeListBuilder.create().texOffs(0, 20)
                .addBox(-width / 2, -height / 2, -length / 2, width, height, length), 0, 13, 0);
        CubeListBuilder skull = CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.5f, -3.5f, -6, 7, 7, 7);
        if (variant == 0) skull.texOffs(32, 0).addBox(-1, 0, -11, 2, 2, 6); // sword-jaw
        if (variant == 3 || variant == 22) skull.texOffs(28, 0).addBox(-6, -2, -7, 12, 6, 4); // taotie maw
        PartDefinition head = add(root, "head", skull, 0, 9, -length / 2);
        int horns = 3 + variant % 6;
        head.addOrReplaceChild("crest", CubeListBuilder.create().texOffs(48, 0)
                .addBox(-1, -horns, -1, 2, horns, 2), PartPose.offset(0, -3, -2));
        switch (variant) {
            case 1 -> root.getChild("body").addOrReplaceChild("armor_ridge", CubeListBuilder.create().texOffs(32, 0)
                    .addBox(-2, -4, -6, 4, 3, 12), PartPose.ZERO);
            case 7 -> root.getChild("body").addOrReplaceChild("tiger_shoulders", CubeListBuilder.create().texOffs(32, 0)
                    .addBox(-7, -3, -5, 14, 5, 5), PartPose.ZERO);
            case 8 -> head.addOrReplaceChild("single_horn", CubeListBuilder.create().texOffs(52, 0)
                    .addBox(-1, -8, -5, 2, 9, 2), PartPose.ZERO);
            case 10 -> root.getChild("body").addOrReplaceChild("wooden_wings", CubeListBuilder.create().texOffs(24, 40)
                    .addBox(-15, -2, -3, 30, 2, 9), PartPose.ZERO);
            case 15 -> head.addOrReplaceChild("great_ears", CubeListBuilder.create().texOffs(36, 0)
                    .addBox(-8, -4, -1, 5, 7, 2).addBox(3, -4, -1, 5, 7, 2), PartPose.ZERO);
            case 16 -> head.addOrReplaceChild("lion_mane", CubeListBuilder.create().texOffs(28, 0)
                    .addBox(-6, -6, -1, 12, 12, 3), PartPose.ZERO);
            case 18, 21 -> head.addOrReplaceChild("branch_antlers", CubeListBuilder.create().texOffs(36, 0)
                    .addBox(-5, -10, -1, 2, 10, 2).addBox(3, -10, -1, 2, 10, 2)
                    .addBox(-8, -8, -1, 5, 2, 2).addBox(3, -6, -1, 6, 2, 2), PartPose.ZERO);
            case 23 -> root.getChild("body").addOrReplaceChild("void_spines", CubeListBuilder.create().texOffs(40, 0)
                    .addBox(-1, -7, -6, 2, 6, 2).addBox(-1, -8, -1, 2, 7, 2)
                    .addBox(-1, -6, 4, 2, 5, 2), PartPose.ZERO);
            default -> { }
        }
        addLeg(root, "front_left_leg", width / 2 - 1, -length / 3, 7 + variant % 4);
        addLeg(root, "front_right_leg", -width / 2 + 1, -length / 3, 7 + variant % 4);
        addLeg(root, "rear_left_leg", width / 2 - 1, length / 3, 7 + variant % 4);
        addLeg(root, "rear_right_leg", -width / 2 + 1, length / 3, 7 + variant % 4);
        CubeListBuilder tails = CubeListBuilder.create().texOffs(40, 24)
                .addBox(-1.5f, -1.5f, 0, 3, 3, 8 + variant % 6);
        if (variant == 13) {
            for (int x = -4; x <= 4; x += 2) tails.addBox(x, -1, 2, 1, 2, 10 + Math.abs(x));
        }
        add(root, "tail", tails, 0, 11, length / 2);
    }

    private static void buildKui(PartDefinition root) {
        add(root, "body", CubeListBuilder.create().texOffs(0, 16).addBox(-6, -6, -7, 12, 12, 14), 0, 11, 0);
        PartDefinition head = add(root, "head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4, -4, -5, 8, 8, 7), 0, 6, -7);
        head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(48, 0)
                .addBox(-1, -8, -1, 2, 8, 2), PartPose.offset(0, -4, -2));
        addLeg(root, "front_left_leg", 0, 0, 12);
        empty(root, "front_right_leg"); empty(root, "rear_left_leg"); empty(root, "rear_right_leg");
        add(root, "tail", CubeListBuilder.create().texOffs(48, 22).addBox(-2, -2, 0, 4, 4, 10), 0, 10, 7);
    }

    private static void buildSerpent(PartDefinition root, int variant) {
        float length = variant == 17 ? 26 : variant == 28 ? 32 : 28;
        add(root, "body", CubeListBuilder.create().texOffs(0, 18)
                .addBox(-4, -4, -length / 2, 8, 8, length), 0, 12, 0);
        PartDefinition head = add(root, "head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-5, -4, -7, 10, 8, 8).texOffs(36, 0).addBox(-3, 0, -11, 6, 3, 4), 0, 10, -length / 2);
        head.addOrReplaceChild("antlers", CubeListBuilder.create().texOffs(52, 0)
                .addBox(-5, -8, -1, 2, 8, 2).addBox(3, -8, -1, 2, 8, 2), PartPose.offset(0, -3, -2));
        addFin(root, "front_left_leg", 4, -8, false); addFin(root, "front_right_leg", -4, -8, true);
        addFin(root, "rear_left_leg", 4, 7, false); addFin(root, "rear_right_leg", -4, 7, true);
        add(root, "tail", CubeListBuilder.create().texOffs(36, 24)
                .addBox(-3, -3, 0, 6, 6, 16), 0, 12, length / 2);
    }

    private static void buildFlyer(PartDefinition root, int variant) {
        boolean bat = variant == 19;
        float bodyLength = variant == 11 ? 12 : 9;
        add(root, "body", CubeListBuilder.create().texOffs(0, 18)
                .addBox(-4, -4, -bodyLength / 2, 8, 8, bodyLength), 0, 12, 0);
        CubeListBuilder face = CubeListBuilder.create().texOffs(0, 0).addBox(-3, -3, -5, 6, 6, 6);
        if (variant == 11) face.addBox(-1, -1, -10, 2, 2, 7);
        if (variant == 19) face.addBox(-7, -7, -1, 5, 7, 2).addBox(2, -7, -1, 5, 7, 2);
        if (variant == 25) face.addBox(-1, -8, -1, 2, 6, 2);
        PartDefinition head = add(root, "head", face, 0, 9, -bodyLength / 2);
        if (variant == 26) head.addOrReplaceChild("sun_crown", CubeListBuilder.create().texOffs(36, 0)
                .addBox(-5, -6, -1, 10, 2, 2).addBox(-1, -10, -1, 2, 10, 2), PartPose.ZERO);
        float span = bat ? 16 : variant == 26 ? 14 : 12;
        addWing(root, "front_left_leg", 4, span, false, bat); addWing(root, "front_right_leg", -4, span, true, bat);
        addLeg(root, "rear_left_leg", 2, 2, 5); addLeg(root, "rear_right_leg", -2, 2, 5);
        if (variant == 26) root.getChild("body").addOrReplaceChild("third_leg", CubeListBuilder.create().texOffs(0, 46)
                .addBox(-1, 0, -1, 2, 6, 2), PartPose.offset(0, 3, 0));
        CubeListBuilder tail = CubeListBuilder.create().texOffs(40, 22).addBox(-2, -1, 0, 4, 2, 10);
        if (variant == 6 || variant == 25) tail.addBox(-5, 0, 3, 3, 1, 11).addBox(2, 0, 3, 3, 1, 11);
        add(root, "tail", tail, 0, 12, bodyLength / 2);
    }

    private static void buildMantis(PartDefinition root) {
        add(root, "body", CubeListBuilder.create().texOffs(0, 20).addBox(-3, -3, -8, 6, 6, 16), 0, 11, 0);
        add(root, "head", CubeListBuilder.create().texOffs(0, 0).addBox(-5, -3, -4, 10, 6, 5), 0, 8, -8);
        addBlade(root, "front_left_leg", 3, false); addBlade(root, "front_right_leg", -3, true);
        addThinLeg(root, "rear_left_leg", 3, 2, false); addThinLeg(root, "rear_right_leg", -3, 2, true);
        add(root, "tail", CubeListBuilder.create().texOffs(32, 30).addBox(-4, -4, 0, 8, 8, 12), 0, 11, 8);
    }

    private static void buildSpider(PartDefinition root) {
        add(root, "body", CubeListBuilder.create().texOffs(0, 20).addBox(-7, -4, -6, 14, 8, 12), 0, 12, 2);
        add(root, "head", CubeListBuilder.create().texOffs(0, 0).addBox(-5, -3, -6, 10, 6, 6), 0, 12, -4);
        addSpiderLeg(root, "front_left_leg", 6, -3, false); addSpiderLeg(root, "front_right_leg", -6, -3, true);
        addSpiderLeg(root, "rear_left_leg", 6, 4, false); addSpiderLeg(root, "rear_right_leg", -6, 4, true);
        add(root, "tail", CubeListBuilder.create().texOffs(38, 0).addBox(-6, -5, 0, 12, 10, 10), 0, 12, 8);
    }

    private static void buildTurtle(PartDefinition root, boolean snakeTail) {
        add(root, "body", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-8, -4, -9, 16, 8, 18).texOffs(0, 42).addBox(-7, -6, -7, 14, 3, 14), 0, 14, 0);
        add(root, "head", CubeListBuilder.create().texOffs(0, 0).addBox(-3, -3, -5, 6, 6, 6), 0, 14, -10);
        addStub(root, "front_left_leg", 7, -5); addStub(root, "front_right_leg", -7, -5);
        addStub(root, "rear_left_leg", 7, 6); addStub(root, "rear_right_leg", -7, 6);
        add(root, "tail", CubeListBuilder.create().texOffs(48, 20)
                .addBox(-1.5f, -1.5f, 0, 3, 3, snakeTail ? 18 : 6), 0, 14, 9);
    }

    private static void buildRabbit(PartDefinition root) {
        add(root, "body", CubeListBuilder.create().texOffs(0, 22).addBox(-5, -5, -5, 10, 10, 11), 0, 13, 1);
        PartDefinition head = add(root, "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -4, -5, 8, 8, 7), 0, 9, -5);
        head.addOrReplaceChild("ears", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-4, -11, -1, 3, 9, 2).addBox(1, -11, -1, 3, 9, 2), PartPose.ZERO);
        addLeg(root, "front_left_leg", 3, -3, 6); addLeg(root, "front_right_leg", -3, -3, 6);
        addLeg(root, "rear_left_leg", 4, 3, 7); addLeg(root, "rear_right_leg", -4, 3, 7);
        add(root, "tail", CubeListBuilder.create().texOffs(40, 20).addBox(-3, -3, 0, 6, 6, 5), 0, 11, 6);
    }

    private static void buildApe(PartDefinition root) {
        add(root, "body", CubeListBuilder.create().texOffs(0, 20).addBox(-7, -8, -5, 14, 16, 10), 0, 10, 0);
        add(root, "head", CubeListBuilder.create().texOffs(0, 0).addBox(-5, -5, -5, 10, 10, 8), 0, 2, -3);
        addArm(root, "front_left_leg", 7, false); addArm(root, "front_right_leg", -7, true);
        addLeg(root, "rear_left_leg", 4, 3, 10); addLeg(root, "rear_right_leg", -4, 3, 10);
        empty(root, "tail");
    }

    private static void buildWarGolem(PartDefinition root) {
        add(root, "body", CubeListBuilder.create().texOffs(0, 20).addBox(-8, -8, -4, 16, 16, 8), 0, 9, 0);
        empty(root, "head");
        addArm(root, "front_left_leg", 9, false); addArm(root, "front_right_leg", -9, true);
        addLeg(root, "rear_left_leg", 4, 2, 11); addLeg(root, "rear_right_leg", -4, 2, 11);
        add(root, "tail", CubeListBuilder.create().texOffs(44, 0).addBox(-5, -5, 0, 10, 10, 3), 0, 9, 4);
    }

    private static PartDefinition add(PartDefinition root, String name, CubeListBuilder cubes, float x, float y, float z) {
        return root.addOrReplaceChild(name, cubes, PartPose.offset(x, y, z));
    }

    private static void empty(PartDefinition root, String name) {
        add(root, name, CubeListBuilder.create(), 0, 0, 0);
    }

    private static void addLeg(PartDefinition root, String name, float x, float z, float height) {
        add(root, name, CubeListBuilder.create().texOffs(0, 46)
                .addBox(-2, 0, -2, 4, height, 4), x, 13, z);
    }

    private static void addWing(PartDefinition root, String name, float x, float span, boolean mirror, boolean bat) {
        CubeListBuilder wing = CubeListBuilder.create().texOffs(28, 38).mirror(mirror)
                .addBox(mirror ? -span : 0, -1, -3, span, 2, bat ? 9 : 12);
        add(root, name, wing, x, 10, -2);
    }

    private static void addFin(PartDefinition root, String name, float x, float z, boolean mirror) {
        add(root, name, CubeListBuilder.create().texOffs(32, 42).mirror(mirror)
                .addBox(mirror ? -7 : 0, -1, -2, 7, 2, 5), x, 12, z);
    }

    private static void addBlade(PartDefinition root, String name, float x, boolean mirror) {
        add(root, name, CubeListBuilder.create().texOffs(36, 0).mirror(mirror)
                .addBox(mirror ? -10 : 0, -1, -2, 10, 2, 4), x, 10, -7);
    }

    private static void addThinLeg(PartDefinition root, String name, float x, float z, boolean mirror) {
        add(root, name, CubeListBuilder.create().texOffs(48, 18).mirror(mirror)
                .addBox(mirror ? -8 : 0, 0, -1, 8, 2, 2), x, 12, z);
    }

    private static void addSpiderLeg(PartDefinition root, String name, float x, float z, boolean mirror) {
        add(root, name, CubeListBuilder.create().texOffs(0, 48).mirror(mirror)
                .addBox(mirror ? -12 : 0, -1, -1, 12, 2, 2)
                .addBox(mirror ? -10 : 8, 0, -1, 2, 7, 2), x, 12, z);
    }

    private static void addStub(PartDefinition root, String name, float x, float z) {
        add(root, name, CubeListBuilder.create().texOffs(0, 48).addBox(-3, 0, -3, 6, 3, 6), x, 15, z);
    }

    private static void addArm(PartDefinition root, String name, float x, boolean mirror) {
        add(root, name, CubeListBuilder.create().texOffs(40, 18).mirror(mirror)
                .addBox(-3, 0, -3, 6, 15, 6), x, 4, 0);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        head.xRot = headPitch * Mth.DEG_TO_RAD;
        float stride = Mth.cos(limbSwing * 0.6662f) * 1.2f * limbSwingAmount;
        float rhythm = 0.09f + variant % 5 * 0.012f;
        frontLeftLeg.xRot = frontRightLeg.xRot = rearLeftLeg.xRot = rearRightLeg.xRot = 0;
        frontLeftLeg.zRot = frontRightLeg.zRot = rearLeftLeg.zRot = rearRightLeg.zRot = 0;
        body.y = 13.0f + Mth.sin(ageInTicks * rhythm) * 0.25f;
        if (variant == 6 || variant == 11 || variant == 19 || variant == 25 || variant == 26) {
            float flap = Mth.cos(ageInTicks * (variant == 19 ? 0.65f : 0.38f)) * 0.8f;
            frontLeftLeg.zRot = -0.35f - flap;
            frontRightLeg.zRot = 0.35f + flap;
            rearLeftLeg.xRot = stride * 0.25f;
            rearRightLeg.xRot = -stride * 0.25f;
            body.y = 11.0f + Mth.sin(ageInTicks * 0.18f) * 0.8f;
        } else if (variant == 4 || variant == 17 || variant == 28) {
            body.yRot = Mth.sin(ageInTicks * 0.08f + variant) * 0.12f;
            frontLeftLeg.zRot = rearLeftLeg.zRot = Mth.sin(ageInTicks * 0.16f) * 0.35f;
            frontRightLeg.zRot = rearRightLeg.zRot = -frontLeftLeg.zRot;
        } else if (variant == 9) {
            float scuttle = Mth.cos(limbSwing * 0.9f) * 0.55f * limbSwingAmount;
            frontLeftLeg.zRot = rearRightLeg.zRot = scuttle;
            frontRightLeg.zRot = rearLeftLeg.zRot = -scuttle;
        } else if (variant == 2) {
            frontLeftLeg.xRot = stride;
            body.y = 11.0f - Math.abs(Mth.sin(limbSwing * 0.6662f)) * 1.5f * limbSwingAmount;
        } else if (variant == 12 || variant == 14) {
            frontLeftLeg.xRot = rearRightLeg.xRot = stride * 0.3f;
            frontRightLeg.xRot = rearLeftLeg.xRot = -stride * 0.3f;
        } else {
            frontLeftLeg.xRot = rearRightLeg.xRot = stride;
            frontRightLeg.xRot = rearLeftLeg.xRot = -stride;
        }
        tail.yRot = Mth.cos(ageInTicks * rhythm + variant) * (0.18f + variant % 4 * 0.04f);
        if (attackTime > 0.0f) {
            float strike = Mth.sin(Mth.sqrt(attackTime) * Mth.PI);
            head.xRot -= strike * 0.9f;
            frontLeftLeg.xRot -= strike * 1.2f;
            frontRightLeg.xRot -= strike * 1.2f;
            tail.xRot = 0.45f - strike * 0.35f;
        } else {
            tail.xRot = 0.45f;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight,
                               int packedOverlay, int color) {
        root.render(poseStack, consumer, packedLight, packedOverlay, color);
    }
}
