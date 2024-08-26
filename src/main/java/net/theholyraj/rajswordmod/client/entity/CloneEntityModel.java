package net.theholyraj.rajswordmod.client.entity;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.Mob;
import net.theholyraj.rajswordmod.world.entity.custom.CloneEntity;

public class CloneEntityModel<C extends Mob> extends PlayerModel<CloneEntity> {
    public CloneEntityModel(ModelPart pRoot, boolean pSlim) {
        super(pRoot, pSlim);
    }
    public static LayerDefinition createBodyLayer() {
        // This creates the mesh definition for the player model
        MeshDefinition meshDefinition = PlayerModel.createMesh(CubeDeformation.NONE, true);
        // Builds the mesh into a LayerDefinition
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

}
