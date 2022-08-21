package dev.sora.sparky.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import dev.sora.sparky.Sparky;
import dev.sora.sparky.client.LayerDefinitionInitializer;
import dev.sora.sparky.client.model.ModelInitializer;
import dev.sora.sparky.common.block.ModChestBlock;
import dev.sora.sparky.common.block.entity.ModChestBlockEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {

  private final ModelPart lid;
  private final ModelPart bottom;
  private final ModelPart lock;

  private final BlockEntityRenderDispatcher renderer;

  public ModChestRenderer(BlockEntityRendererProvider.Context context) {
    ModelPart modelPart = context.bakeLayer(LayerDefinitionInitializer.MOD_CHEST);

    this.renderer = context.getBlockEntityRenderDispatcher();
    this.bottom = modelPart.getChild("mod_chest_bottom");
    this.lid = modelPart.getChild("mod_chest_lid");
    this.lock = modelPart.getChild("mod_chest_lock");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshDefinition = new MeshDefinition();
    PartDefinition partDefinition = meshDefinition.getRoot();

    partDefinition.addOrReplaceChild("mod_chest_bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
    partDefinition.addOrReplaceChild("mod_chest_lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
    partDefinition.addOrReplaceChild("mod_chest_lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 8.0F, 0.0F));

    return LayerDefinition.create(meshDefinition, 64, 64);
  }

  @Override
  public void render(T tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
    ModChestBlockEntity tileEntity = (ModChestBlockEntity) tileEntityIn;

    Level level = tileEntity.getLevel();
    boolean useTileEntityBlockState = level != null;

    BlockState blockState = useTileEntityBlockState ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().defaultBlockState().setValue(ModChestBlock.FACING, Direction.SOUTH);
    Block block = blockState.getBlock();

    if (block instanceof ModChestBlock modChestBlock) {
      poseStack.pushPose();

      float f = blockState.getValue(ModChestBlock.FACING).toYRot();

      poseStack.translate(0.5D, 0.5D, 0.5D);
      poseStack.mulPose(Vector3f.YP.rotationDegrees(-f));
      poseStack.translate(-0.5D, -0.5D, -0.5D);

      DoubleBlockCombiner.NeighborCombineResult<? extends ModChestBlockEntity> neighborCombineResult;

      if (useTileEntityBlockState) {
        neighborCombineResult = modChestBlock.combine(blockState, level, tileEntityIn.getBlockPos(), true);
      } else {
        neighborCombineResult = DoubleBlockCombiner.Combiner::acceptNone;
      }

      float openness = neighborCombineResult.<Float2FloatFunction>apply(ModChestBlock.opennessCombiner(tileEntity)).get(partialTicks);
      openness = 1.0F - openness;
      openness = 1.0F - openness * openness * openness;

      int brightness = neighborCombineResult.<Int2IntFunction>apply(new BrightnessCombiner<>()).applyAsInt(combinedLightIn);

      Material material = new Material(Sheets.CHEST_SHEET, ModelInitializer.MOD_CHEST);

      VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityCutout);

      this.render(poseStack, vertexConsumer, this.lid, this.lock, this.bottom, openness, brightness, combinedOverlayIn);

      poseStack.popPose();

    }
  }

  private void render(PoseStack poseStack, VertexConsumer vertexConsumer, ModelPart lid, ModelPart lock, ModelPart bottom, float openness, int brightness, int combinedOverlayIn) {
    lid.xRot = -(openness * ((float) Math.PI / 2F));
    lock.xRot = lid.xRot;

    lid.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
    lock.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
    bottom.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
  }
}
